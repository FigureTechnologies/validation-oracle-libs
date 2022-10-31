package tech.figure.validationoracle.client.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import tech.figure.validationoracle.client.domain.serialization.CosmWasmBigIntegerToUintSerializer
import tech.figure.validationoracle.client.domain.serialization.CosmWasmUintToBigIntegerDeserializer

/**
 * A configuration for a verifier's interactions with the validation oracle smart 11contract.
 *
 * @param validationCosts A numeric representation of a specified coin amount to be taken during onboarding.  This value
 * will be distributed to the verifier and its fee destinations based on those configurations.
 * @param validationType A unique name that defines the type of validation that will be performed by a validator.
 * @param validator Entity detail of validator that is going to perform the validation.
 */
@JsonNaming(SnakeCaseStrategy::class)
data class ValidatorConfiguration(
    @JsonSerialize(using = CosmWasmBigIntegerToUintSerializer::class)
    @JsonDeserialize(using = CosmWasmUintToBigIntegerDeserializer::class)
    val validationCosts: List<ValidationCost> = emptyList(),
    val validationType: String,
    val validator: EntityDetail,
)
