package tech.figure.validationoracle.client.domain.execute

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import tech.figure.validationoracle.client.domain.execute.base.ContractExecute
import tech.figure.validationoracle.client.domain.serialization.ExecuteCreateValidationDefinitionSerializer

/**
 * This class is a reflection of the request body used in the validation oracle smart contract's add validation
 * definition execution route.
 *
 * To use it, simply create the execute class and call the appropriate function:
 * ```kotlin
 * val execute = CreateValidationDefinition(validationType, displayName, validators, enabled = true)
 * val txResponse = VOClient.addValidationDefinition(execute, signer, options)
 * ```
 *
 * @param validationType A pretty human-readable name for this validation type.
 * @param displayName A pretty human-readable name for this validation type (vs a typically snake_case validation_type name).
 * @param validators All validators that support validating this specific validation type.
 * @param enabled Whether or not this validation type will accept incoming onboard requests.  If left null, the default value used will be `true`.  This parameter can only be changed by the contract owner once initialized.
 * @param bindName Whether or not to bind the name value creating an validation definition.
 */
@JsonSerialize(using = ExecuteCreateValidationDefinitionSerializer::class)
data class CreateValidationDefinition(
    val validationType: String,
    val displayName: String? = null,
    val enabled: Boolean? = null,
    val bindName: Boolean? = null,
) : ContractExecute
