package tech.figure.validationoracle.client.domain.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Defines access routes for a specific entity in the validation oracle smart contract.
 *
 * @param ownerAddress The bech32 address that exposed these access routes.
 * @param accessRoutes A collection of routes that have exposed validation data.
 * @param definitionType Specifies the type of entity that exposed these routes.
 */
@JsonNaming(SnakeCaseStrategy::class)
data class AccessDefinition(
    val ownerAddress: String,
    val accessRoutes: List<AccessRoute>,
    val definitionType: AccessDefinitionType,
)

/**
 * Entity types that can specify access routes.
 */
enum class AccessDefinitionType {
    /** The address that onboarded a specific scope. */
    @JsonProperty("requestor")
    REQUESTOR,

    /** The address that the requestor specified for this validation. */
    @JsonProperty("validator")
    VALIDATOR,
}
