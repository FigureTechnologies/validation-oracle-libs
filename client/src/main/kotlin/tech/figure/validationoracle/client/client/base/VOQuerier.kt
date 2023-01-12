package tech.figure.validationoracle.client.client.base

import tech.figure.validationoracle.client.domain.model.ContractInfo
import tech.figure.validationoracle.client.domain.model.ValidationDefinition
import tech.figure.validationoracle.client.domain.model.ValidationRequestOrder

/**
 * VOQuerier = Validation Oracle Querier
 * This interface defines the different query routes for the validation oracle smart contract.
 */
interface VOQuerier {
    /**
     * Retrieves the bech32 address assigned to the validation oracle smart contract based on the current
     * environment.
     */
    fun queryContractAddress(): String

    /**
     * Retrieves validation request orders currently stored in the smart contract which a given validator is or was
     * allowed to validate.
     */
    fun queryContractInfo(): ContractInfo

    /**
     * Retrieves a single validation definition currently stored in the smart contract by its type.
     */
    fun queryValidationDefinitionByType(validationType: String): ValidationDefinition?

    /**
     * Retrieves a single validation request order currently stored in the smart contract by its ID.
     */
    fun queryValidationRequestById(id: String): ValidationRequestOrder?

    /**
     * Retrieves validation request orders currently stored in the smart contract owned by the given address.
     */
    fun queryValidationRequestsByOwner(ownerAddress: String): List<ValidationRequestOrder>? // TODO: Needs to be nullable?

    /**
     * Retrieves validation request orders currently stored in the smart contract which a given validator is or was
     * allowed to validate.
     */
    fun queryValidationRequestsByValidator(validatorAddress: String): List<ValidationRequestOrder>? // TODO: Needs to be nullable?
}
