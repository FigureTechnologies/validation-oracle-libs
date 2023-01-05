package tech.figure.validationoracle.client.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import cosmos.base.v1beta1.CoinOuterClass.Coin

/**
 * The structure used for storing validation requests in the validation oracle smart contract.
 *
 * @param id The ID of the validation request. It must be unique within the contract instance.
 * @param owner The bech32 address of the requestor.
 * @param scopes A list of the Provenance scopes, each denoted by its bech32 address, that are expected
 * to be validated in order for this request to be fulfilled.
 * @param allowedValidators An optional list of bech32 addresses corresponding to parties which are
 * permitted to fulfill this request. If omitted, the contract will allow any Provenance
 * address to accept the request as a validator.
 * @param quote The quote the requestor is offering in exchange for completion of the request.
 * @param status The status of the validation request.
 */
@JsonNaming(SnakeCaseStrategy::class)
data class ValidationRequestOrder(
    val id: String,
    val owner: String,
    val scopes: List<String>,
    val allowedValidators: List<String>? = null,
    val quote: List<Coin>,
    val status: ValidationRequestStatus,
)
