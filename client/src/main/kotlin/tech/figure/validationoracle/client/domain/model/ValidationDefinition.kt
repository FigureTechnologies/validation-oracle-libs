package tech.figure.validationoracle.client.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * The structure used for storing validation definitions in the validation oracle smart contract.
 *
 * @param validationType A unique name that defines the type of validation that will be performed by a validator.
 * @param displayName An optional human-readable name for this validation type.
 * @param enabled Whether new [ValidationRequest]s which use this definition can be created or not.
 * Managed by the contract admin.
 */
@JsonNaming(SnakeCaseStrategy::class)
data class ValidationDefinition(
    val validationType: String,
    val displayName: String? = null,
    val enabled: Boolean,
)
