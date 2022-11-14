package tech.figure.validationoracle.client.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import tech.figure.validationoracle.client.domain.serialization.CosmWasmBigIntegerToUintSerializer
import tech.figure.validationoracle.client.domain.serialization.CosmWasmUintToBigIntegerDeserializer
import java.math.BigInteger

/**
 * Defines a collector for fees for a validator.  All fee destinations should have fee percents totaling 100%, and the
 * contract runs validation to ensure that this is the case, so it can be assumed true in all cases.
 *
 * @param amount A value without decimal places that indicates how much coin this fee destination receives.
 * @param denom The denomination of coin required for this fee.
 * @param destination Details of entity receiving fee.
 */
@JsonNaming(SnakeCaseStrategy::class)
data class ValidationCost(
    @JsonSerialize(using = CosmWasmBigIntegerToUintSerializer::class)
    @JsonDeserialize(using = CosmWasmUintToBigIntegerDeserializer::class)
    val amount: BigInteger,
    val denom: String,
    val destination: EntityDetail,
)
