package tech.figure.validationoracle.client.client.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import cosmwasm.wasm.v1.QueryOuterClass
import io.provenance.client.grpc.PbClient
import io.provenance.client.protobuf.extensions.queryWasm
import tech.figure.validationoracle.client.domain.model.ValidationDefinition
import tech.figure.validationoracle.client.client.base.VOQuerier
import tech.figure.validationoracle.client.domain.NullContractResponseException
import tech.figure.validationoracle.client.domain.query.QueryValidationDefinition
import tech.figure.validationoracle.client.domain.query.QueryValidationDefinitions
import tech.figure.validationoracle.client.domain.query.base.ContractQuery

/**
 * The default override of an [VOQuerier].  Provides all the standard functionality to use an [ACClient][tech.figure.validationoracle.client.client.base.VOClient] if an override for
 * business logic is not necessary.
 */
class DefaultVOQuerier(
    private val contractIdentifier: tech.figure.validationoracle.client.client.base.ContractIdentifier,
    private val objectMapper: ObjectMapper,
    private val pbClient: PbClient,
) : VOQuerier {
    private companion object {
        private val VALIDATION_DEFINITION_LIST_TYPE_REFERENCE = object : TypeReference<List<ValidationDefinition>>() {}
//        // Static instance of a TypeReference for List<AssetScopeAttribute> to prevent multiple instantiations
//        private val SCOPE_ATTRIBUTE_LIST_TYPE_REFERENCE = object : TypeReference<List<AssetScopeAttribute>>() {}
    }

//    /**
//     * This value is cached via a lazy initializer to prevent re-running code against the blockchain after the contract
//     * address has been resolved.  The contract address should never change, so this value only needs to be fetched a
//     * single time and can be re-used.
//     */
    private val cachedContractAddress by lazy { contractIdentifier.resolveAddress(pbClient) }

    override fun queryContractAddress(): String = cachedContractAddress

    override fun queryValidationDefinitionByAssetTypeAndValidationTypeOrNull(
        assetType: String,
        validationType: String,
        throwExceptions: Boolean,
    ): ValidationDefinition? = doQueryOrNull(
        query = QueryValidationDefinition(assetType, validationType),
        throwExceptions = throwExceptions,
    )

    override fun queryValidationDefinitionByAssetTypeAndValidationType(
        assetType: String,
        validationType: String,
    ): ValidationDefinition = doQuery(
        query = QueryValidationDefinition(
            assetType = assetType,
            validationType = validationType,
        ),
    )

    override fun queryValidationDefinitions(): List<ValidationDefinition> = doQuery(
        query = QueryValidationDefinitions,
        typeReference = VALIDATION_DEFINITION_LIST_TYPE_REFERENCE,
    )

//    override fun queryAssetScopeAttributeByAssetUuidOrNull(
//        assetUuid: UUID,
//        assetType: String,
//        throwExceptions: Boolean,
//    ): AssetScopeAttribute? = doQueryOrNull(
//        query = QueryAssetScopeAttributeForAssetType(
//            identifier = AssetIdentifier.AssetUuid(value = assetUuid),
//            assetType = assetType
//        ),
//        throwExceptions = throwExceptions,
//    )
//
//    override fun queryAssetScopeAttributeByAssetUuid(
//        assetUuid: UUID,
//        assetType: String,
//    ): AssetScopeAttribute = doQuery(
//        query = QueryAssetScopeAttributeForAssetType(
//            identifier = AssetIdentifier.AssetUuid(value = assetUuid),
//            assetType = assetType,
//        ),
//    )
//
//    override fun queryAssetScopeAttributeByScopeAddressOrNull(
//        scopeAddress: String,
//        assetType: String,
//        throwExceptions: Boolean,
//    ): AssetScopeAttribute? = doQueryOrNull(
//        query = QueryAssetScopeAttributeForAssetType(
//            identifier = AssetIdentifier.ScopeAddress(value = scopeAddress),
//            assetType = assetType,
//        ),
//        throwExceptions = throwExceptions,
//    )
//
//    override fun queryAssetScopeAttributeByScopeAddress(
//        scopeAddress: String,
//        assetType: String,
//    ): AssetScopeAttribute = doQuery(
//        query = QueryAssetScopeAttributeForAssetType(
//            identifier = AssetIdentifier.ScopeAddress(value = scopeAddress),
//            assetType = assetType,
//        )
//    )
//
//    override fun queryAssetScopeAttributesByAssetUuidOrNull(
//        assetUuid: UUID,
//        throwExceptions: Boolean,
//    ): List<AssetScopeAttribute>? = doQueryOrNull(
//        query = QueryAssetScopeAttributes(
//            identifier = AssetIdentifier.AssetUuid(value = assetUuid),
//        ),
//        throwExceptions = throwExceptions,
//        typeReference = SCOPE_ATTRIBUTE_LIST_TYPE_REFERENCE,
//    )
//
//    override fun queryAssetScopeAttributesByAssetUuid(
//        assetUuid: UUID,
//    ): List<AssetScopeAttribute> = doQuery(
//        query = QueryAssetScopeAttributes(
//            identifier = AssetIdentifier.AssetUuid(value = assetUuid),
//        ),
//        typeReference = SCOPE_ATTRIBUTE_LIST_TYPE_REFERENCE,
//    )
//
//    override fun queryAssetScopeAttributesByScopeAddressOrNull(
//        scopeAddress: String,
//        throwExceptions: Boolean,
//    ): List<AssetScopeAttribute>? = doQueryOrNull(
//        query = QueryAssetScopeAttributes(
//            identifier = AssetIdentifier.ScopeAddress(value = scopeAddress),
//        ),
//        throwExceptions = throwExceptions,
//        typeReference = SCOPE_ATTRIBUTE_LIST_TYPE_REFERENCE,
//    )
//
//    override fun queryAssetScopeAttributesByScopeAddress(
//        scopeAddress: String,
//    ): List<AssetScopeAttribute> = doQuery(
//        query = QueryAssetScopeAttributes(
//            identifier = AssetIdentifier.ScopeAddress(value = scopeAddress),
//        ),
//        typeReference = SCOPE_ATTRIBUTE_LIST_TYPE_REFERENCE,
//    )
//
//    override fun queryFeePaymentsByAssetUuidOrNull(
//        assetUuid: UUID,
//        assetType: String,
//        throwExceptions: Boolean,
//    ): FeePaymentDetail? = doQueryOrNull(
//        query = QueryFeePayments(
//            identifier = AssetIdentifier.AssetUuid(value = assetUuid),
//            assetType = assetType,
//        ),
//        throwExceptions = throwExceptions,
//    )
//
//    override fun queryFeePaymentsByAssetUuid(
//        assetUuid: UUID,
//        assetType: String,
//    ): FeePaymentDetail = doQuery(
//        query = QueryFeePayments(
//            identifier = AssetIdentifier.AssetUuid(value = assetUuid),
//            assetType = assetType,
//        )
//    )
//
//    override fun queryFeePaymentsByScopeAddressOrNull(
//        scopeAddress: String,
//        assetType: String,
//        throwExceptions: Boolean,
//    ): FeePaymentDetail? = doQueryOrNull(
//        query = QueryFeePayments(
//            identifier = AssetIdentifier.ScopeAddress(value = scopeAddress),
//            assetType = assetType,
//        ),
//        throwExceptions = throwExceptions,
//    )
//
//    override fun queryFeePaymentsByScopeAddress(
//        scopeAddress: String,
//        assetType: String,
//    ): FeePaymentDetail = doQuery(
//        query = QueryFeePayments(
//            identifier = AssetIdentifier.ScopeAddress(value = scopeAddress),
//            assetType = assetType,
//        )
//    )
//
//    override fun queryContractState(): ACContractState = doQuery(
//        query = QueryState,
//    )
//
//    override fun queryContractVersion(): ACVersionInfo = doQuery(
//        query = QueryVersion,
//    )
//
    /**
     * Executes a provided [ContractQuery] against the validation oracle smart contract.  This relies on the
     * internalized [PbClient] to do the heavy lifting.
     */
    private inline fun <reified T : ContractQuery, reified U : Any> doQuery(
        query: T,
        typeReference: TypeReference<U>? = null,
    ): U = doQueryOrNull(query = query, typeReference = typeReference)
        ?: throw NullContractResponseException("Received null response from validation oracle smart contract for: ${query.queryFailureMessage}")

    private inline fun <reified T : ContractQuery, reified U : Any> doQueryOrNull(
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
     * Executes a provided [ContractQuery] against the validation oracle smart contract with additional functionality
     * designed to return null responses when requested.
     */
    private inline fun <reified T : ContractQuery, reified U : Any> doQueryOrNull(
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
