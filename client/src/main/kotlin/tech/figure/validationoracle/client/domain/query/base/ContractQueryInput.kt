package tech.figure.validationoracle.client.domain.query.base

import tech.figure.validationoracle.client.domain.ContractActionInput

/**
 * A simple interface that should be tagged on all contract queries.  This prevents invalid classes from being used
 * as query targets to keep the code honest.
 */
interface ContractQueryInput : ContractActionInput {
    /**
     * Allows the query to define a message when a null response is returned by the contract. Improves client
     * consumer experience with a more readable response upon failure.
     */
    val queryDescription: String
}
