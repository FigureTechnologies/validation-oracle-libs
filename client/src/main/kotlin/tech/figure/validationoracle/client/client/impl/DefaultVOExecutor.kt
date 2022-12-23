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
import tech.figure.validationoracle.client.domain.execute.CreateValidationDefinition
import tech.figure.validationoracle.client.domain.execute.RequestValidation
import tech.figure.validationoracle.client.domain.execute.base.ContractExecute

/**
 * The default implementation of an [VOExecutor].  Provides all the standard functionality to use a
 * [VOClient][tech.figure.validationoracle.client.client.base.VOClient]
 * if an override for business logic is not necessary.
 */
class DefaultVOExecutor(
    private val objectMapper: ObjectMapper,
    private val pbClient: PbClient,
    private val querier: VOQuerier,
) : VOExecutor {
    private fun generateAddValidationDefinitionMsg(
        execute: CreateValidationDefinition,
        signerAddress: String,
    ): MsgExecuteContract = generateMsg(execute, signerAddress)

    override fun addValidationDefinition(
        execute: CreateValidationDefinition,
        signer: Signer,
        options: BroadcastOptions,
    ): BroadcastTxResponse = doExecute(generateAddValidationDefinitionMsg(execute, signer.address()), signer, options)

    private fun generateAddValidationDefinitionMsg(
        execute: RequestValidation,
        signerAddress: String,
    ): MsgExecuteContract = generateMsg(execute, signerAddress)

    override fun requestValidationExecute(
        execute: RequestValidation,
        signer: Signer,
        options: BroadcastOptions,
    ): BroadcastTxResponse = doExecute(generateAddValidationDefinitionMsg(execute, signer.address()), signer, options)

    /**
     * Constructs a generic [MsgExecuteContract] from a provided [ContractExecute] message, ensuring that the provided
     * address is the signer.
     */
    private fun <T : ContractExecute> generateMsg(
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
     * Executes a provided [MsgExecuteContract] with the provided signer information and broadcast mode. This relies
     * on the internalized [PbClient] to do the heavy lifting.
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
            if (response.txResponse.code != 0) {
                throw IllegalStateException(
                    "Validation oracle contract execution failed with message:" +
                        "${System.lineSeparator()}${response.txResponse.rawLog}"
                )
            }
        }
    }
}
