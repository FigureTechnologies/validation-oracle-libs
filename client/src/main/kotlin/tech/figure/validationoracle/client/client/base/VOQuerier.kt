package tech.figure.validationoracle.client.client.base

import tech.figure.validationoracle.client.domain.model.ValidationDefinition
import tech.figure.validationoracle.client.domain.model.ValidationRequestOrder
import tech.figure.validationoracle.client.domain.query.ValidationDefinitionTypeQuery
import tech.figure.validationoracle.client.domain.query.ValidationRequestIdQuery

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
     * Retrieves a single validation definition currently stored in the smart contract by its type.
     */
    fun queryValidationDefinitionByType(query: ValidationDefinitionTypeQuery): ValidationDefinition?

    /**
     * Retrieves a single validation request order currently stored in the smart contract by its ID.
     */
    fun queryValidationRequestById(query: ValidationRequestIdQuery): ValidationRequestOrder?
}
