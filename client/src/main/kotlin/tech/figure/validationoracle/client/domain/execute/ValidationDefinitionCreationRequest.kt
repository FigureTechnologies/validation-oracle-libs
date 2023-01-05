package tech.figure.validationoracle.client.domain.execute

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import tech.figure.validationoracle.client.domain.execute.base.ContractExecuteInput
import tech.figure.validationoracle.client.domain.serialization.ValidationDefinitionCreationRequestSerializer

/**
 * This class is a reflection of the request body used in the validation oracle smart contract's execution route for
 * adding a validation definition.
 *
 * To use it, simply create an instance of the execution class and call the appropriate function:
 * ```kotlin
 * val payload = ValidationDefinitionCreationRequest("some_validation_type", "Some Type", true, true)
 * val txResponse = voClient.addValidationDefinition(payload, signer, options)
 * ```
 *
 * @param validationType The type of validation the definition will correspond to.
 * @param displayName A pretty human-readable name for this validation type.
 * @param enabled Whether new [validation requests][ValidationRequest] which use this will accept incoming onboard
 * requests. If left null, will default to `true`.
 * This parameter can only be changed by the contract owner once initialized.
 * @param bindName Whether to bind the name value creating a validation definition.
 */
@JsonSerialize(using = ValidationDefinitionCreationRequestSerializer::class)
data class ValidationDefinitionCreationRequest(
    val validationType: String,
    val displayName: String? = null,
    val enabled: Boolean? = null, // Let non-null defaults be handled at the smart contract level
    val bindName: Boolean? = null,
) : ContractExecuteInput
