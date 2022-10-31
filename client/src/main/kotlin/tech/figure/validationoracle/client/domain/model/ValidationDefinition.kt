package tech.figure.validationoracle.client.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * The root structure for defining how a validation should be onboarded by the validation oracle smart contract.
 *
 * @param validationType A unique name that defines the type of validation that will be performed by a validator.
 * @param displayName A pretty human-readable name for this validation type (vs a typically snake_case validation_type name).
 * @param validators All different validations' information for this specific validation type.
 * @param enabled Whether or not this validation type will accept incoming onboard requests.  If left null, the default value used will be `true`.  This parameter can only be changed by the contract owner.
 */
@JsonNaming(SnakeCaseStrategy::class)
data class ValidationDefinition(
    val validationType: String,
    val displayName: String?,
    val validators: List<ValidatorConfiguration>,
    val enabled: Boolean,
)
