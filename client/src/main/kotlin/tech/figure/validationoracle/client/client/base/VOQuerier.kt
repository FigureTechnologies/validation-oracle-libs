package tech.figure.validationoracle.client.client.base

import tech.figure.validationoracle.client.domain.model.ValidationDefinition
import tech.figure.validationoracle.client.domain.model.ValidationRequestOrder
import tech.figure.validationoracle.client.domain.query.QueryValidationDefinitionByType
import tech.figure.validationoracle.client.domain.query.QueryValidationRequestById

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
     * Retrieves a single validation definition by type currently stored in the smart contract.
     */
    fun queryValidationDefinitionByType(queryValidationDefinitionByType: QueryValidationDefinitionByType): ValidationDefinition?

    /**
     * Retrieves all validation request order instances currently available for this smart contract.
     */
    fun queryValidationRequestById(queryValidationRequestOrder: QueryValidationRequestById): ValidationRequestOrder?
}
