package tech.figure.validationoracle.client.domain.execute.base

import tech.figure.validationoracle.client.domain.ContractAction

/**
 * A simple interface that should be tagged on all contract executions.  This prevents invalid classes from being used
 * as execute targets to keep the code honest.
 */
interface ContractExecute : ContractAction
