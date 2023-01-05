package tech.figure.validationoracle.client.client.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.provenance.client.grpc.PbClient
import tech.figure.validationoracle.client.client.base.VOClient
import tech.figure.validationoracle.client.client.base.VOExecutor
import tech.figure.validationoracle.client.client.base.VOQuerier

/**
 * The default implementation for an [VOClient]. Allows the client to be a composition of its various elements.
 * Use [VOClient.getDefault] to retrieve an instance of this.
 */
class DefaultVOClient(
    override val pbClient: PbClient,
    override val objectMapper: ObjectMapper,
    private val executor: VOExecutor,
    private val querier: VOQuerier,
) : VOClient, VOExecutor by executor, VOQuerier by querier
