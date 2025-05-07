package tech.figure.validationoracle.client.client.impl

import com.fasterxml.jackson.databind.ObjectMapper
import cosmos.base.v1beta1.CoinOuterClass
import cosmos.tx.v1beta1.ServiceOuterClass.BroadcastTxResponse
import cosmwasm.wasm.v1.Tx.MsgExecuteContract
import io.provenance.client.grpc.BaseReqSigner
import io.provenance.client.grpc.PbClient
import io.provenance.client.grpc.Signer
import io.provenance.client.protobuf.extensions.getBaseAccount
import io.provenance.client.protobuf.extensions.toAny
import io.provenance.client.protobuf.extensions.toTxBody
import tech.figure.validationoracle.client.client.base.BroadcastOptions
import tech.figure.validationoracle.client.client.base.VOExecutor
import tech.figure.validationoracle.client.client.base.VOQuerier
import tech.figure.validationoracle.client.domain.execute.ContractSettingsUpdate
import tech.figure.validationoracle.client.domain.execute.EntityCreationRequest
import tech.figure.validationoracle.client.domain.execute.EntityUpdateRequest
import tech.figure.validationoracle.client.domain.execute.ValidationDefinitionCreationRequest
import tech.figure.validationoracle.client.domain.execute.ValidationDefinitionDeletionRequest
import tech.figure.validationoracle.client.domain.execute.ValidationDefinitionUpdateRequest
import tech.figure.validationoracle.client.domain.execute.ValidationRequest
import tech.figure.validationoracle.client.domain.execute.ValidationRequestDeletion
import tech.figure.validationoracle.client.domain.execute.ValidationRequestUpdate
import tech.figure.validationoracle.client.domain.execute.base.ContractExecuteInput

/**
 * The default implementation of an [VOExecutor]. Provides all the standard functionality to use a
 * [VOClient][tech.figure.validationoracle.client.client.base.VOClient]
 * if an override for business logic is not necessary.
 */
class DefaultVOExecutor(
    private val objectMapper: ObjectMapper,
    private val pbClient: PbClient,
    private val querier: VOQuerier,
) : VOExecutor {

    override fun createValidationDefinition(
        request: ValidationDefinitionCreationRequest,
        signer: Signer,
        options: BroadcastOptions,
    ): BroadcastTxResponse = doExecute(generateMsg(request, signer.address()), signer, options)

    override fun updateValidationDefinition(
        request: ValidationDefinitionUpdateRequest,
        signer: Signer,
        options: BroadcastOptions,
    ): BroadcastTxResponse = doExecute(generateMsg(request, signer.address()), signer, options)

    override fun deleteValidationDefinition(
        request: ValidationDefinitionDeletionRequest,
        signer: Signer,
        options: BroadcastOptions,
    ): BroadcastTxResponse = doExecute(generateMsg(request, signer.address()), signer, options)

    override fun createRequestForValidation(
        request: ValidationRequest,
        signer: Signer,
        options: BroadcastOptions,
    ): BroadcastTxResponse = doExecute(generateMsg(request, signer.address()), signer, options)

    override fun updateRequestForValidation(
        request: ValidationRequestUpdate,
        signer: Signer,
        options: BroadcastOptions,
    ): BroadcastTxResponse = doExecute(generateMsg(request, signer.address()), signer, options)

    override fun deleteRequestForValidation(
        request: ValidationRequestDeletion,
        signer: Signer,
        options: BroadcastOptions,
    ): BroadcastTxResponse = doExecute(generateMsg(request, signer.address()), signer, options)

    override fun createEntity(
        request: EntityCreationRequest,
        signer: Signer,
        options: BroadcastOptions,
    ): BroadcastTxResponse = doExecute(generateMsg(request, signer.address()), signer, options)

    override fun updateEntity(
        request: EntityUpdateRequest,
        signer: Signer,
        options: BroadcastOptions,
    ): BroadcastTxResponse = doExecute(generateMsg(request, signer.address()), signer, options)

    override fun updateContractSettings(
        request: ContractSettingsUpdate,
        signer: Signer,
        options: BroadcastOptions,
    ): BroadcastTxResponse = doExecute(generateMsg(request, signer.address()), signer, options)

    /**
     * Constructs a generic [MsgExecuteContract] from a provided [ContractExecuteInput] message,
     * ensuring that the provided address is the signer.
     */
    private fun <T : ContractExecuteInput> generateMsg(
        executeMsg: T,
        signerAddress: String,
        funds: CoinOuterClass.Coin? = null,
    ): MsgExecuteContract = MsgExecuteContract.newBuilder().also { executeContract ->
        executeContract.msg = executeMsg.toBase64Msg(objectMapper)
        executeContract.contract = querier.queryContractAddress()
        executeContract.sender = signerAddress
        funds?.also { executeContract.addFunds(it) }
    }.build()

    /**
     * Executes a provided [MsgExecuteContract] with the provided signer information and broadcast mode.
     * This relies on the internal [PbClient] to do the heavy lifting.
     */
    private fun doExecute(
        msg: MsgExecuteContract,
        signer: Signer,
        options: BroadcastOptions,
    ): BroadcastTxResponse {
        val signerAddress = signer.address()
        val account = options.baseAccount ?: pbClient.authClient.getBaseAccount(signerAddress)
        return pbClient.estimateAndBroadcastTx(
            txBody = msg.toAny().toTxBody(),
            signers = BaseReqSigner(
                signer = signer,
                sequenceOffset = options.sequenceOffset,
                account = account,
            ).let(::listOf),
            mode = options.broadcastMode,
        ).also { response ->
            check(response.txResponse.code != 0) {
                "Validation oracle contract execution failed with message:" +
                    "${System.lineSeparator()}${response.txResponse.rawLog}"
            }
        }
    }
}
