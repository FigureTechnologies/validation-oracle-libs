package tech.figure.validationoracle.client.domain.execute.base

import tech.figure.validationoracle.client.domain.ContractActionInput

/**
 * A simple interface that should be tagged on all inputs to contract executions.
 * This prevents invalid classes from being used as execution targets, to keep the code honest.
 */
interface ContractExecuteInput : ContractActionInput
