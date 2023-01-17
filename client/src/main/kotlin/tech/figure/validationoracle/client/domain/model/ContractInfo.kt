package tech.figure.validationoracle.client.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * @param admin The bech32 Provenance address of the contract instance's administrator.
 * @param bindName The Provenance name bound to the contract instance's address.
 * @param contractName The human-readable name of the contract instance.
 * @param contractType The name of the contract source code package.
 * @param contractVersion The version of the contract source code package.
 * @param createRequestNhashFee The fee charged by the contract for creating a validation request.
 */
@JsonNaming(SnakeCaseStrategy::class)
data class ContractInfo(
    val admin: String,
    val bindName: String,
    val contractName: String,
    val contractType: String,
    val contractVersion: String,
    val createRequestNhashFee: UInt,
)
