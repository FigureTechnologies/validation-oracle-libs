package tech.figure.validationoracle.client.domain.execute

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import cosmos.base.v1beta1.CoinOuterClass.Coin
import tech.figure.validationoracle.client.domain.execute.base.ContractExecuteInput
import tech.figure.validationoracle.client.domain.serialization.ValidationRequestSerializer

/**
 * This class is a reflection of the request body used in the validation oracle smart contract's request validation
 * execution route.
 *
 * To use it, simply create an instance of the execution class and call the appropriate function:
 * ```kotlin
 * val execute = ValidationRequest(validationType, displayName, validators, enabled = true)
 * val txResponse = voClient.requestValidationExecute(execute, signer, options)
 * ```
 *
 * @param id The ID of the validation request.
 * @param scopes A list of the Provenance scopes, each denoted by its bech32 address, that are expected to be validated
 * in order for this request to be fulfilled.
 * @param allowedValidators An optional list of bech32 addresses corresponding to the parties which are permitted to
 * fulfill this request.
 * If omitted, the contract will allow any Provenance address to accept the request as a validator.
 * @param quote The quote the requestor is offering in exchange for completion of the request.
 */
@JsonSerialize(using = ValidationRequestSerializer::class)
data class ValidationRequest(
    val id: String,
    val scopes: List<String>,
    val allowedValidators: List<String>? = null,
    val quote: List<Coin>,
) : ContractExecuteInput
