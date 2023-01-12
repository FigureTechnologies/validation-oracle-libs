package tech.figure.validationoracle.client.client

import io.kotest.core.spec.Order
import io.kotest.core.spec.Spec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import io.provenance.client.grpc.GasEstimationMethod
import io.provenance.client.grpc.PbClient
import tech.figure.validationoracle.client.client.base.ContractIdentifier
import tech.figure.validationoracle.client.client.base.VOClient
import tech.figure.validationoracle.client.test.IntegrationTestBase
import tech.figure.validationoracle.client.test.PrimitiveArbs.anyNonEmptyString
import java.net.URI

@Order(1)
class ContractMetadataIntegrationTest : IntegrationTestBase({
    val pbClient = PbClient(
        chainId = "chain-local",
        channelUri = URI("http://localhost:9090"),
        gasEstimationMethod = GasEstimationMethod.MSG_FEE_CALCULATION,
    )
    autoClose(pbClient)
    val voClient = VOClient.getDefault(
        ContractIdentifier.Name("vo.sc.pb"),
        pbClient,
    )
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
            // TODO: Implement
        }
    }
    "Updating the contract's settings" xshould {
        "fail when given an empty body" {
            // TODO: Implement
        }
        "fail when given an invalid new admin address" {
            // TODO: Implement
        }
        "succeed when given a valid request" {
            // TODO: Implement
        }
    }
}) {
    override val specTestContainers: List<SpecTestContainer> = listOf(
        SpecTestContainer.PROVENANCE,
    )

    /**
     * Starts the Docker Compose environment before the start of the first [Spec].
     *
     * TODO: this function would ideally belong in the parent class and be invoked at "before all specs" instead
     */
    override suspend fun beforeSpec(spec: Spec) {
        instance.stop()
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
