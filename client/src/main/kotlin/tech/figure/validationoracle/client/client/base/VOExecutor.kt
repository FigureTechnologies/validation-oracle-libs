package tech.figure.validationoracle.client.client.base

import cosmos.tx.v1beta1.ServiceOuterClass.BroadcastTxResponse
import io.provenance.client.grpc.Signer
import tech.figure.validationoracle.client.domain.execute.CreateValidationDefinition
import tech.figure.validationoracle.client.domain.execute.RequestValidation

/**
 * VOExecutor = Validation Oracle Executor
 * This interface defines the different execution routes for the validation oracle smart contract, as well as
 * generation functions for the underlying contract execution messages for when the caller desires to collect properly-
 * formatted execution messages for a batched transaction.
 */
interface VOExecutor {
    /**
     * Executes the validation oracle smart contract to add a new validation definition to allow a new validation type to be
     * registered with the smart contract.
     *
     * @param execute The [CreateValidationDefinition] payload that will be sent to the smart contract.
     * @param signer Any implementation of [Signer] to sign the message programmatically.  See [AccountSigner][tech.figure.validationoracle.util.wallet.AccountSigner] for a provided implementation.
     * @param options Various options that alter how the transaction is broadcast.  See [BroadcastOptions] for more details.
     */
    fun addValidationDefinition(
        execute: CreateValidationDefinition,
        signer: Signer,
        options: BroadcastOptions = BroadcastOptions(),
    ): BroadcastTxResponse

    /**
     * Executes the validation oracle smart contract to execute an add VO request.
     *
     * @param execute The [RequestValidation] payload that will be sent to the smart contract.
     * @param signer Any implementation of [Signer] to sign the message programmatically.  See [AccountSigner][tech.figure.validationoracle.util.wallet.AccountSigner] for a provided implementation.
     * @param options Various options that alter how the transaction is broadcast.  See [BroadcastOptions] for more details.
     */
    fun requestValidationExecute(
        execute: RequestValidation,
        signer: Signer,
        options: BroadcastOptions = BroadcastOptions(),
    ): BroadcastTxResponse
}
