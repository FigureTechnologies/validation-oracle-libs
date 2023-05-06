package tech.figure.validationoracle.client.client

import io.grpc.StatusRuntimeException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.Order
import io.kotest.core.spec.Spec
import io.kotest.inspectors.forAtLeastOne
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.property.checkAll
import tech.figure.validationoracle.client.domain.execute.ContractSettingsUpdate
import tech.figure.validationoracle.client.test.IntegrationTestBase
import tech.figure.validationoracle.client.test.PrimitiveArbs.anyBech32TestnetAddress
import tech.figure.validationoracle.client.test.PrimitiveArbs.anyBlankString
import tech.figure.validationoracle.client.test.PrimitiveArbs.anyNonEmptyString

@Order(1)
class ContractMetadataIntegrationTest : IntegrationTestBase({
    val voClient = getVoClient()
    "After initialization, the contract" should {
        "be queryable for basic metadata" {
            voClient.queryContractAddress() shouldNotBe null // TODO: Do regex validation?
            // Check that the query result matches what was set in the contract instantiation script
            voClient.queryContractInfo().let { contractInfo ->
                // TODO: Add pre-generated account for contract administrator to Provenance seed directory
                contractInfo.bindName shouldBe "vo.sc.pb"
                contractInfo.contractName shouldBe "Validation Oracle - Local demo for integration testing"
                // We skip checking the contract name and type because those are specific to the contract's version
                contractInfo.createRequestNhashFee shouldBe 3000U
            }
        }
        "have no validation definitions defined yet" {
            checkAll(
                anyNonEmptyString,
            ) { randomValidationType ->
                voClient.queryValidationDefinitionByType(randomValidationType) shouldBe null
            }
        }
        "have no validation requests defined yet" {
            checkAll(
                anyNonEmptyString,
            ) { randomString ->
                voClient.queryValidationRequestById(randomString) shouldBe null
                voClient.queryValidationRequestsByOwner(randomString) shouldBe emptyList()
                voClient.queryValidationRequestsByValidator(randomString) shouldBe emptyList()
            }
        }
        "have no entities defined yet" {
            checkAll(
                anyBech32TestnetAddress,
            ) { randomAddress ->
                voClient.queryEntityByAddress(randomAddress) shouldBe null
            }
        }
    }
    "Updating the contract's settings" should {
        "fail when given an invalid new admin address" {
            checkAll(
                anyBlankString,
            ) { randomInvalidAddress ->
                shouldThrow<StatusRuntimeException> {
                    voClient.updateContractSettings(
                        ContractSettingsUpdate(
                            newAdminAddress = randomInvalidAddress,
                        ),
                        SpecTestContainer.Provenance.contractAdminAccount,
                    )
                }.let { exception ->
                    listOf(
                        "invalid request: failed to execute message; message index: 0: " +
                            "Invalid request: new_admin_address was empty: execute wasm contract failed",
                        // TODO: In the contract code, see if error below can be captured into the error above
                        "invalid request: failed to execute message; message index: 0: Generic error: addr_validate " +
                            "errored: empty address string is not allowed: execute wasm contract failed",
                    ).forAtLeastOne { errorMessage ->
                        exception.message shouldContain errorMessage
                    }
                }
            }
        }
        "!succeed when given a valid request" {
            // TODO: Implement
        }
    }
}) {
    /**
     * Starts the Docker Compose environment before the start of the first [Spec].
     *
     * TODO: this function would ideally belong in the parent class and be invoked at "before all specs" instead
     */
    override suspend fun beforeSpec(spec: Spec) {
        instance.withServices(
            *specTestContainers.map { container -> container.serviceName }.toTypedArray(),
        )
        specTestContainers.forEach { testContainer ->
            instance.waitingFor(testContainer.serviceName, testContainer.waitStrategy)
        }
        instance.start()
        super.beforeSpec(spec)
    }
}
