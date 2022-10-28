package tech.figure.validationoracle.client.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Base properties for the validation oracle smart contract.  These values should be considered constants.
 *
 * @param baseContractName The name that the contract owns.  All validation attributes are based on names that
 * use this value as the suffix.  Ex: base name = "asset", then a heloc would have the attribute "heloc.asset"
 * @param admin The bech32 address of the contract administrator.  Certain execution routes are only allowed to be run
 * when the admin is the signer for the message.
 */
@JsonNaming(SnakeCaseStrategy::class)
data class VOContractState(val baseContractName: String, val admin: String)
