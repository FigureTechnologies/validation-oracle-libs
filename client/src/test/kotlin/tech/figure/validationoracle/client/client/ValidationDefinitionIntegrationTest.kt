package tech.figure.validationoracle.client.client

import io.grpc.StatusRuntimeException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.Order
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldContainIgnoringCase
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.orNull
import io.kotest.property.checkAll
import tech.figure.validationoracle.client.domain.execute.ValidationDefinitionCreationRequest
import tech.figure.validationoracle.client.test.IntegrationTestBase
import tech.figure.validationoracle.client.test.PrimitiveArbs.anyBlankString
import tech.figure.validationoracle.client.test.PrimitiveArbs.anyNonEmptyString

@Order(2)
class ValidationDefinitionIntegrationTest : IntegrationTestBase({
    val voClient = getVoClient()
    "Creating a new validation definition" should {
        "!fail on an invalid validation type" {
            checkAll(
                anyBlankString,
                anyNonEmptyString.orNull(),
                Arb.boolean().orNull(),
                Arb.boolean().orNull(),
            ) { invalidValidationType, displayName, enabled, bindName ->
                shouldThrow<StatusRuntimeException> {
                    voClient.createValidationDefinition(
                        ValidationDefinitionCreationRequest(
                            validationType = invalidValidationType,
                            displayName = displayName,
                            enabled = enabled,
                            bindName = bindName,
                        ),
                        SpecTestContainer.Provenance.contractAdminAccount,
                    )
                }.let { exception ->
                    // TODO: Re-enable this test case once contract is updated to require a non-empty validation type
                    exception.message shouldContain "invalid request: failed to execute message; "
                }
            }
        }
        "fail if executed by a party other than the contract admin" {
            checkAll(
                anyNonEmptyString,
                anyNonEmptyString.orNull(),
                Arb.boolean().orNull(),
                Arb.boolean().orNull(),
            ) { validationType, displayName, enabled, bindName ->
                shouldThrow<StatusRuntimeException> {
                    voClient.createValidationDefinition(
                        ValidationDefinitionCreationRequest(
                            validationType = validationType,
                            displayName = displayName,
                            enabled = enabled,
                            bindName = bindName,
                        ),
                        SpecTestContainer.Provenance.party1Account,
                    )
                }.let { exception ->
                    exception.message shouldContainIgnoringCase "invalid request: failed to execute message; " +
                        "message index: 0: Unauthorized: must be the contract admin: execute wasm contract failed"
                }
            }
        }
        "!succeed with a valid request body" {
            // TODO: Implement
        }
    }
})
