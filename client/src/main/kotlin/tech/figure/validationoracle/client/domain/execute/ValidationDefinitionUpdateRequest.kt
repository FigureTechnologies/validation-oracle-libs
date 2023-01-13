package tech.figure.validationoracle.client.domain.execute

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import tech.figure.validationoracle.client.domain.execute.base.ContractExecuteInput
import tech.figure.validationoracle.client.domain.serialization.ValidationDefinitionUpdateRequestSerializer

/**
 * This class is a reflection of the request body used in the validation oracle smart contract's execution route for
 * updating an existing validation definition.
 *
 * @param currentValidationType The validation type of the existing definition that will be updated.
 * @param newValidationType An optional new validation type for the definition.
 * @param newDisplayName An optional new display name for the definition.
 * @param enabled Whether new [validation requests][ValidationRequest] which use this definition will accept incoming
 * onboard requests. If left null, will default to `true`.
 */
@JsonSerialize(using = ValidationDefinitionUpdateRequestSerializer::class)
data class ValidationDefinitionUpdateRequest(
    val currentValidationType: String,
    val newValidationType: String? = null,
    val newDisplayName: String? = null,
    val enabled: Boolean? = null, // Defaults should be handled at the smart contract level
) : ContractExecuteInput
