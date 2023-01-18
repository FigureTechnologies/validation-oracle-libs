package tech.figure.validationoracle.client.client.base

import cosmos.tx.v1beta1.ServiceOuterClass.BroadcastTxResponse
import io.provenance.client.grpc.Signer
import tech.figure.validationoracle.client.domain.execute.ContractSettingsUpdate
import tech.figure.validationoracle.client.domain.execute.EntityCreationRequest
import tech.figure.validationoracle.client.domain.execute.EntityUpdateRequest
import tech.figure.validationoracle.client.domain.execute.ValidationDefinitionCreationRequest
import tech.figure.validationoracle.client.domain.execute.ValidationDefinitionDeletionRequest
import tech.figure.validationoracle.client.domain.execute.ValidationDefinitionUpdateRequest
import tech.figure.validationoracle.client.domain.execute.ValidationRequest
import tech.figure.validationoracle.client.domain.execute.ValidationRequestDeletion
import tech.figure.validationoracle.client.domain.execute.ValidationRequestUpdate

/**
 * VOExecutor = Validation Oracle Executor.
 * This interface defines the different execution routes for the validation oracle smart contract, as well as
 * generation functions for the underlying contract execution messages for when the caller desires to collect properly-
 * formatted execution messages for a batched transaction.
 */
interface VOExecutor {
    /**
     * Executes the validation oracle smart contract to create a new validation definition to allow a new validation
     * type to be used.
     *
     * @param request The [ValidationDefinitionCreationRequest] payload that will be sent to the smart contract.
     * @param signer Any implementation of [Signer] to sign the message programmatically.
     * See [AccountSigner][tech.figure.validationoracle.util.wallet.AccountSigner] for a provided implementation.
     * @param options Various options that alter how the transaction is broadcast.
     * See [BroadcastOptions] for more details.
     */
    fun createValidationDefinition(
        request: ValidationDefinitionCreationRequest,
        signer: Signer,
        options: BroadcastOptions = BroadcastOptions(),
    ): BroadcastTxResponse

    /**
     * Executes the validation oracle smart contract to update an existing validation definition.
     *
     * @param request The [ValidationDefinitionUpdateRequest] payload that will be sent to the smart contract.
     * @param signer Any implementation of [Signer] to sign the message programmatically.
     * See [AccountSigner][tech.figure.validationoracle.util.wallet.AccountSigner] for a provided implementation.
     * @param options Various options that alter how the transaction is broadcast.
     * See [BroadcastOptions] for more details.
     */
    fun updateValidationDefinition(
        request: ValidationDefinitionUpdateRequest,
        signer: Signer,
        options: BroadcastOptions = BroadcastOptions(),
    ): BroadcastTxResponse

    /**
     * Executes the validation oracle smart contract to delete a validation definition.
     *
     * @param request The [ValidationDefinitionDeletionRequest] payload that will be sent to the smart contract.
     * @param signer Any implementation of [Signer] to sign the message programmatically.
     * See [AccountSigner][tech.figure.validationoracle.util.wallet.AccountSigner] for a provided implementation.
     * @param options Various options that alter how the transaction is broadcast.
     * See [BroadcastOptions] for more details.
     */
    fun deleteValidationDefinition(
        request: ValidationDefinitionDeletionRequest,
        signer: Signer,
        options: BroadcastOptions = BroadcastOptions(),
    ): BroadcastTxResponse

    /**
     * Executes the validation oracle smart contract to create a new request for validation.
     *
     * @param request The [ValidationRequest] payload that will be sent to the smart contract.
     * @param signer Any implementation of [Signer] to sign the message programmatically.
     * See [AccountSigner][tech.figure.validationoracle.util.wallet.AccountSigner] for a provided implementation.
     * @param options Various options that alter how the transaction is broadcast.
     * See [BroadcastOptions] for more details.
     */
    fun createRequestForValidation(
        request: ValidationRequest,
        signer: Signer,
        options: BroadcastOptions = BroadcastOptions(),
    ): BroadcastTxResponse

    /**
     * Executes the validation oracle smart contract to update an existing request for validation.
     *
     * @param request The [ValidationRequestUpdate] payload that will be sent to the smart contract.
     * @param signer Any implementation of [Signer] to sign the message programmatically.
     * See [AccountSigner][tech.figure.validationoracle.util.wallet.AccountSigner] for a provided implementation.
     * @param options Various options that alter how the transaction is broadcast.
     * See [BroadcastOptions] for more details.
     */
    fun updateRequestForValidation(
        request: ValidationRequestUpdate,
        signer: Signer,
        options: BroadcastOptions = BroadcastOptions(),
    ): BroadcastTxResponse

    /**
     * Executes the validation oracle smart contract to delete a validation request.
     *
     * @param request The [ValidationRequestDeletion] payload that will be sent to the smart contract.
     * @param signer Any implementation of [Signer] to sign the message programmatically.
     * See [AccountSigner][tech.figure.validationoracle.util.wallet.AccountSigner] for a provided implementation.
     * @param options Various options that alter how the transaction is broadcast.
     * See [BroadcastOptions] for more details.
     */
    fun deleteRequestForValidation(
        request: ValidationRequestDeletion,
        signer: Signer,
        options: BroadcastOptions = BroadcastOptions(),
    ): BroadcastTxResponse

    /**
     * Executes the validation oracle smart contract to create a new entity for participation in the validation process.
     *
     * @param request The [EntityCreationRequest] payload that will be sent to the smart contract.
     * @param signer Any implementation of [Signer] to sign the message programmatically.
     * See [AccountSigner][tech.figure.validationoracle.util.wallet.AccountSigner] for a provided implementation.
     * @param options Various options that alter how the transaction is broadcast.
     * See [BroadcastOptions] for more details.
     */
    fun createEntity(
        request: EntityCreationRequest,
        signer: Signer,
        options: BroadcastOptions = BroadcastOptions(),
    ): BroadcastTxResponse

    /**
     * Executes the validation oracle smart contract to update an existing entity.
     *
     * @param request The [EntityUpdateRequest] payload that will be sent to the smart contract.
     * @param signer Any implementation of [Signer] to sign the message programmatically.
     * See [AccountSigner][tech.figure.validationoracle.util.wallet.AccountSigner] for a provided implementation.
     * @param options Various options that alter how the transaction is broadcast.
     * See [BroadcastOptions] for more details.
     */
    fun updateEntity(
        request: EntityUpdateRequest,
        signer: Signer,
        options: BroadcastOptions = BroadcastOptions(),
    ): BroadcastTxResponse

    /**
     * Executes the validation oracle smart contract to update its settings.
     *
     * @param request The [EntityUpdateRequest] payload that will be sent to the smart contract.
     * @param signer Any implementation of [Signer] to sign the message programmatically.
     * See [AccountSigner][tech.figure.validationoracle.util.wallet.AccountSigner] for a provided implementation.
     * @param options Various options that alter how the transaction is broadcast.
     * See [BroadcastOptions] for more details.
     */
    fun updateContractSettings(
        request: ContractSettingsUpdate,
        signer: Signer,
        options: BroadcastOptions = BroadcastOptions(),
    ): BroadcastTxResponse
}
