package tech.figure.validationoracle.client.domain.execute

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import cosmos.base.v1beta1.CoinOuterClass.Coin
import tech.figure.validationoracle.client.domain.execute.base.ContractExecuteInput
import tech.figure.validationoracle.client.domain.serialization.ValidationRequestUpdateSerializer

@JsonSerialize(using = ValidationRequestUpdateSerializer::class)
data class ValidationRequestUpdate(
    val currentId: String,
    val newId: String? = null,
    val newScopes: List<String>? = null,
    val newAllowedValidators: List<String>? = null,
    val newQuote: List<Coin>? = null,
) : ContractExecuteInput
