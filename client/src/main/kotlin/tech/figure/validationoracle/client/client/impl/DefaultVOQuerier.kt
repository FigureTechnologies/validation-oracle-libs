package tech.figure.validationoracle.client.client.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import cosmwasm.wasm.v1.QueryOuterClass
import io.provenance.client.grpc.PbClient
import io.provenance.client.protobuf.extensions.queryWasm
import tech.figure.validationoracle.client.client.base.VOQuerier
import tech.figure.validationoracle.client.domain.NullContractResponseException
import tech.figure.validationoracle.client.domain.model.ContractInfo
import tech.figure.validationoracle.client.domain.model.ValidationDefinition
import tech.figure.validationoracle.client.domain.model.ValidationRequestOrder
import tech.figure.validationoracle.client.domain.query.ContractInfoQuery
import tech.figure.validationoracle.client.domain.query.ValidationDefinitionTypeQuery
import tech.figure.validationoracle.client.domain.query.ValidationRequestIdQuery
import tech.figure.validationoracle.client.domain.query.ValidationRequestOwnerQuery
import tech.figure.validationoracle.client.domain.query.ValidationRequestValidatorQuery
import tech.figure.validationoracle.client.domain.query.base.ContractQueryInput

/**
 * The default override of an [VOQuerier]. Provides all the standard functionality to use a
 * [VOClient][tech.figure.validationoracle.client.client.base.VOClient]
 * if an override for business logic is not necessary.
 */
class DefaultVOQuerier(
    private val contractIdentifier: tech.figure.validationoracle.client.client.base.ContractIdentifier,
    private val objectMapper: ObjectMapper,
    private val pbClient: PbClient,
) : VOQuerier {

    private val cachedContractAddress by lazy { contractIdentifier.resolveAddress(pbClient) }

    override fun queryContractAddress(): String = cachedContractAddress

    override fun queryContractInfo(): ContractInfo? = doQueryOrNull(
        query = ContractInfoQuery
    )

    override fun queryValidationDefinitionByType(validationType: String): ValidationDefinition? = doQueryOrNull(
        query = ValidationDefinitionTypeQuery(type = validationType)
    )

    override fun queryValidationRequestById(id: String): ValidationRequestOrder? = doQueryOrNull(
        query = ValidationRequestIdQuery(id = id)
    )

    override fun queryValidationRequestsByOwner(ownerAddress: String): List<ValidationRequestOrder>? = doQueryOrNull(
        query = ValidationRequestOwnerQuery(owner = ownerAddress)
    )

    override fun queryValidationRequestsByValidator(validatorAddress: String): List<ValidationRequestOrder>? =
        doQueryOrNull(
            query = ValidationRequestValidatorQuery(validator = validatorAddress)
        )

    /**
     * Executes a provided [ContractQueryInput] against the validation oracle smart contract. This relies on the
     * internalized [PbClient] to do the heavy lifting.
     */
    private inline fun <reified T : ContractQueryInput, reified U : Any> doQuery(
        query: T,
        typeReference: TypeReference<U>? = null,
    ): U = doQueryOrNull(query = query, typeReference = typeReference)
        ?: throw NullContractResponseException(
            "Received null response from validation oracle smart contract for ${query.queryDescription}"
        )

    private inline fun <reified T : ContractQueryInput, reified U : Any> doQueryOrNull(
        query: T,
        typeReference: TypeReference<U>? = null,
    ): U? =
        pbClient.wasmClient.queryWasm(
            QueryOuterClass.QuerySmartContractStateRequest.newBuilder()
                .setAddress(queryContractAddress())
                .setQueryData(query.toBase64Msg(objectMapper))
                .build()
        ).data
            .toByteArray()
            .let { array ->
                if (typeReference != null) {
                    objectMapper.readValue(array, typeReference)
                } else {
                    objectMapper.readValue(array, U::class.java)
                }
            }
    /**
     * Executes a provided [ContractQueryInput] against the validation oracle smart contract with additional
     * functionality designed to return null responses when requested.
     */
    private inline fun <reified T : ContractQueryInput, reified U : Any> doQueryOrNull(
        query: T,
        throwExceptions: Boolean,
        typeReference: TypeReference<U>? = null,
    ): U? = try {
        doQueryOrNull(
            query = query,
            typeReference = typeReference,
        )
    } catch (e: Exception) {
        when {
            // Only re-throw caught exceptions if that functionality is requested
            throwExceptions -> throw e
            else -> null
        }
    }
}
