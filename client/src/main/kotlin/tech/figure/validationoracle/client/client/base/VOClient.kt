package tech.figure.validationoracle.client.client.base

import com.fasterxml.jackson.databind.ObjectMapper
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import io.provenance.client.grpc.ChannelOpts
import io.provenance.client.grpc.PbClient
import io.provenance.client.grpc.PbGasEstimator
import tech.figure.validationoracle.client.client.impl.DefaultVOClient
import tech.figure.validationoracle.client.client.impl.DefaultVOExecutor
import tech.figure.validationoracle.client.client.impl.DefaultVOQuerier
import tech.figure.validationoracle.util.objects.VOObjectMapperUtil
import java.net.URI

/**
 * VOClient = Validation Oracle Client.
 * This client defines all the functionality exposed for communicating with the validation oracle smart contract.
 * See comments on the various interfaces for their specific actions or utilities.
 */
interface VOClient : VOExecutor, VOQuerier {
    val pbClient: PbClient
    val objectMapper: ObjectMapper

    companion object {
        // Ensure that the default object mapper is only instantiated a single time to speed up code execution
        private val DEFAULT_OBJECT_MAPPER by lazy { VOObjectMapperUtil.getObjectMapper() }

        /**
         * Standard implementation of an VOClient using a [ContractIdentifier] and [PbClient] for contract communication.
         * If standard communication with the contract is desired without extra business logic during communication
         * phases, this function is sufficient to use.
         *
         * @param contractIdentifier Denotes the name or address of the contract.
         * Tells the client where to point its requests.
         * @param pbClient Provenance's slick client to communicate with provenance resources.
         * @param objectMapper The Jackson [ObjectMapper] instance used to communicate with the contract.
         * The default is configured appropriately, but can be overridden here if necessary.
         */
        fun getDefault(
            contractIdentifier: ContractIdentifier,
            pbClient: PbClient,
            objectMapper: ObjectMapper = DEFAULT_OBJECT_MAPPER,
        ): VOClient = DefaultVOQuerier(contractIdentifier, objectMapper, pbClient).let { querier ->
            DefaultVOClient(
                pbClient = pbClient,
                objectMapper = objectMapper,
                executor = DefaultVOExecutor(objectMapper, pbClient, querier),
                querier = querier,
            )
        }

        /**
         * Full constructor for an [VOClient], including a provided [PbClient] based on matching constructor arguments.
         *
         * @param contractIdentifier Denotes the name or address of the contract. Tells the client where to point its requests.
         * @param chainId The blockchain identifier for use in the [PbClient].
         * @param channelUri The address for which to use for GRPC communication with Provenance.
         * @param gasEstimator Denotes the strategy used to determine gas and fees when doing provenance transactions.
         * @param opts Various GRPC options - see [PbClient] for full description.
         * @param objectMapper The Jackson [ObjectMapper] instance used to communicate with the contract.
         * The default is configured appropriately, but can be overridden here if necessary.
         * @param channelConfigLambda Any additional GRPC configuration desired for the channel contained within the [PbClient].
         */
        fun getDefault(
            contractIdentifier: ContractIdentifier,
            chainId: String,
            channelUri: URI,
            gasEstimator: PbGasEstimator,
            opts: ChannelOpts = ChannelOpts(),
            objectMapper: ObjectMapper = DEFAULT_OBJECT_MAPPER,
            channelConfigLambda: (NettyChannelBuilder) -> Unit = {},
        ): VOClient = PbClient(
            chainId = chainId,
            channelUri = channelUri,
            gasEstimationMethod = gasEstimator,
            opts = opts,
            channelConfigLambda = channelConfigLambda,
        ).let { pbClient ->
            getDefault(
                contractIdentifier = contractIdentifier,
                pbClient = pbClient,
                objectMapper = objectMapper,
            )
        }
    }
}
