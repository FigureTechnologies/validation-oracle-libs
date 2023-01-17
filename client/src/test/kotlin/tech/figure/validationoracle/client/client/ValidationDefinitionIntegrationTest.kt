package tech.figure.validationoracle.client.client

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.Order
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.orNull
import io.kotest.property.checkAll
import tech.figure.validationoracle.client.domain.execute.ValidationDefinitionCreationRequest
import tech.figure.validationoracle.client.test.IntegrationTestBase
import tech.figure.validationoracle.client.test.PrimitiveArbs.anyBlankString
import tech.figure.validationoracle.client.test.PrimitiveArbs.anyNonEmptyString
import tech.figure.validationoracle.util.enums.ProvenanceNetworkType
import tech.figure.validationoracle.util.wallet.AccountSigner

@Order(2)
class ValidationDefinitionIntegrationTest : IntegrationTestBase({
    val voClient = getVoClient()
    "Creating a new validation definition" xshould {
        "fail on an invalid validation type" {
            checkAll(
                anyBlankString,
                anyNonEmptyString.orNull(),
                Arb.boolean().orNull(),
                Arb.boolean().orNull(),
            ) { invalidValidationType, displayName, enabled, bindName ->
                shouldThrow<Exception> { // TODO: Check for a more specific type
                    voClient.createValidationDefinition(
                        ValidationDefinitionCreationRequest(
                            validationType = invalidValidationType,
                            displayName = displayName,
                            enabled = enabled,
                            bindName = bindName,
                        ),
                        AccountSigner.fromMnemonic("", ProvenanceNetworkType.TESTNET), // TODO: Set mnemonic
                    )
                }.let { exception ->
                    // TODO: Validate the exception message
                }
            }
        }
        "succeed with a valid request body" {
            // TODO: Implement
        }
    }
})
