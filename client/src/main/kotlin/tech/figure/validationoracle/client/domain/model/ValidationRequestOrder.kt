package tech.figure.validationoracle.client.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import cosmos.base.v1beta1.CoinOuterClass

/**
 * The root structure for defining how a validation should be onboarded by the validation oracle smart contract.
 *
 * @param id A unique name that defines the type of validation that will be performed by a validator.
 * @param scopes Scope included in VO request.
 * @param quote Quote to perform validation request.
 */
@JsonNaming(SnakeCaseStrategy::class)
data class ValidationRequestOrder(
    val id: String,
    val scopes: List<String> = emptyList(),
    val quote: List<CoinOuterClass.Coin> = emptyList()
)
