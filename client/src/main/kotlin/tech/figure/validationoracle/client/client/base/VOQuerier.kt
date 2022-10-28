package tech.figure.validationoracle.client.client.base

import tech.figure.validationoracle.client.domain.model.ValidationDefinition

/**
 * ACQuerier = Validation Oracle Querier
 * This interface defines the different query routes for the validation oracle smart contract.
 */
interface VOQuerier {
    /**
     * Retrieves the bech32 address assigned to the validation oracle smart contract based on the current
     * environment.
     */
    fun queryContractAddress(): String

    /**
     * Retrieves an asset definition, if present, from the smart contract by attempting a lookup by asset type.
     * If the definition is not present, null is returned.
     *
     * For a more clear description of its values and purpose, see the [ValidationDefinition] class comments.
     *
     * @param assetType The asset type that acts as a unique identifier for the [ValidationDefinition].
     * @param throwExceptions If an exception occurs in the smart contract or with the [PbClient][io.provenance.client.grpc.PbClient], it will be re-thrown
     * unless this value is set to false.  An exception is normally thrown by the smart contract when the asset
     * definition is missing, but that exception will be ignored and null will be returned instead, regardless of this
     * boolean's value.
     */
    fun queryValidationDefinitionByAssetTypeAndValidationTypeOrNull(assetType: String, validationType: String, throwExceptions: Boolean = false): ValidationDefinition?

    /**
     * Retrieves an asset definition, if present, from the smart contract by attempting a lookup by asset type.
     * If the definition is not present or any other error occurs, an exception will be thrown.
     *
     * For a more clear description of its values and purpose, see the [ValidationDefinition] class comments.
     *
     * @param assetType The asset type that acts as a unique identifier for the [ValidationDefinition].
     */
    fun queryValidationDefinitionByAssetTypeAndValidationType(assetType: String, validationType: String): ValidationDefinition

    /**
     * Retrieves all asset definitions current stored in the smart contract.
     * This function can be used to dynamically establish a list of all available values without having foreknowledge
     * of each asset type and scope spec address associated with the smart contract.
     */
    fun queryValidationDefinitions(): List<ValidationDefinition>

//    /**
//     * Retrieves a scope attribute, if present, on a scope by referencing the scope's asset uuid and the type of asset
//     * related to the attribute (ex: mortgage.asset).
//     * If the attribute is not present, null is returned.
//     *
//     * For a more clear description of its values and purpose, see the [AssetScopeAttribute] class comments.
//     *
//     * @param assetUuid The asset uuid that correlates to the scope address.
//     * @param assetType The type of asset, linking the attribute to its attribute name (ex: heloc.asset).  If the asset
//     * exists, but does not have this type, the result of the query will be null.
//     * @param throwExceptions If an exception occurs in the smart contract or with the [PbClient][io.provenance.client.grpc.PbClient], it will be re-thrown
//     * unless this value is set to false.  An exception is normally thrown by the smart contract when the scope
//     * attribute is missing, but that exception will be ignored and null will be returned instead, regardless of this
//     * boolean's value.
//     */
//    fun queryAssetScopeAttributeByAssetUuidOrNull(
//        assetUuid: UUID,
//        assetType: String,
//        throwExceptions: Boolean = false,
//    ): AssetScopeAttribute?
//
//    /**
//     * Retrieves a scope attribute, if present, on a scope by referencing the scope's asset uuid and the type of asset
//     * related to the attribute (ex: mortgage.asset).
//     * If the attribute is not present or any other error occurs, an exception will be thrown.
//     *
//     * For a more clear description of its values and purpose, see the [AssetScopeAttribute] class comments.
//     *
//     * @param assetUuid The asset uuid that correlates to the scope address.
//     * @param assetType The type of asset, linking the attribute to its attribute name (ex: heloc.asset).  If the asset
//     * exists, but does not have this type, this function will throw an exception.
//     */
//    fun queryAssetScopeAttributeByAssetUuid(assetUuid: UUID, assetType: String): AssetScopeAttribute
//
//    /**
//     * Retrieves a scope attribute, if present, on a scope by directly referencing the scope's bech32 address and the
//     * type of asset related to the attribute (ex: mortgage.asset).
//     * If the attribute is not present, null is returned.
//     *
//     * For a more clear description of its values and purpose, see the [AssetScopeAttribute] class comments.
//     *
//     * @param scopeAddress The bech32 address assigned to the scope.  Begins with "scope"
//     * @param assetType The type of asset, linking the attribute to its attribute name (ex: heloc.asset).  If the asset
//     * exists, but does not have this type, the result of the query will be null.
//     * @param throwExceptions If an exception occurs in the smart contract or with the [PbClient][io.provenance.client.grpc.PbClient], it will be re-thrown
//     * unless this value is set to false.  An exception is normally thrown by the smart contract when the scope
//     * attribute is missing, but that exception will be ignored and null will be returned instead, regardless of this
//     * boolean's value.
//     */
//    fun queryAssetScopeAttributeByScopeAddressOrNull(
//        scopeAddress: String,
//        assetType: String,
//        throwExceptions: Boolean = false,
//    ): AssetScopeAttribute?
//
//    /**
//     * Retrieves a scope attribute, if present, on a scope by directly referencing the scope's bech32 address and the
//     * type of asset related to the attribute (ex: mortgage.asset).
//     * If the attribute is not present or any other error occurs, an exception will be thrown.
//     *
//     * For a more clear description of its values and purpose, see the [AssetScopeAttribute] class comments.
//     *
//     * @param scopeAddress The bech32 address assigned to the scope.  Begins with "scope"
//     * @param assetType The type of asset, linking the attribute to its attribute name (ex: heloc.asset).  If the asset
//     * exists, but does not have this type, this function will throw an exception.
//     */
//    fun queryAssetScopeAttributeByScopeAddress(scopeAddress: String, assetType: String): AssetScopeAttribute
//
//    /**
//     * Retrieves all asset scope attributes related to an asset by referencing the scope's UUID.  This can include
//     * multiple results to accommodate the fact that multiple asset types may be associated with an asset.
//     * If no attributes are present, an empty list will be returned.  If an error occurs, null will be returned.
//     *
//     * @param assetUuid The asset uuid that correlates to the scope address.
//     * @param throwExceptions If an exception occurs in the smart contract or with the [PbClient][io.provenance.client.grpc.PbClient], it will be re-thrown
//     * unless this value is set to false.  An exception is normally thrown by the smart contract when the scope
//     * attribute is missing, but that exception will be ignored and null will be returned instead, regardless of this
//     * boolean's value.
//     */
//    fun queryAssetScopeAttributesByAssetUuidOrNull(
//        assetUuid: UUID,
//        throwExceptions: Boolean = false,
//    ): List<AssetScopeAttribute>?
//
//    /**
//     * Retrieves all asset scope attributes related to an asset by referencing the scope's UUID.  This can include
//     * multiple results to accommodate the fact that multiple asset types may be associated with an asset.
//     * If no attributes are present, an empty list will be returned.  If an error occurs, an exception will be thrown.
//     *
//     * @param assetUuid The asset uuid that correlates to the scope address.
//     */
//    fun queryAssetScopeAttributesByAssetUuid(assetUuid: UUID): List<AssetScopeAttribute>
//
//    /**
//     * Retrieves all asset scope attributes related to an asset by directly referencing the scope's address.  This can include
//     * multiple results to accommodate the fact that multiple asset types may be associated with an asset.
//     * If no attributes are present, an empty list will be returned.  If an error occurs, null will be returned.
//     *
//     * @param scopeAddress The bech32 address assigned to the scope.  Begins with "scope"
//     * @param throwExceptions If an exception occurs in the smart contract or with the [PbClient][io.provenance.client.grpc.PbClient], it will be re-thrown
//     * unless this value is set to false.  An exception is normally thrown by the smart contract when the scope
//     * attribute is missing, but that exception will be ignored and null will be returned instead, regardless of this
//     * boolean's value.
//     */
//    fun queryAssetScopeAttributesByScopeAddressOrNull(
//        scopeAddress: String,
//        throwExceptions: Boolean = false,
//    ): List<AssetScopeAttribute>?
//
//    /**
//     * Retrieves all asset scope attributes related to an asset by directly referencing the scope's address.  This can include
//     * multiple results to accommodate the fact that multiple asset types may be associated with an asset.
//     * If no attributes are present, an empty list will be returned.  If an error occurs, an exception will be thrown.
//     *
//     * @param scopeAddress The bech32 address assigned to the scope.  Begins with "scope"
//     */
//    fun queryAssetScopeAttributesByScopeAddress(scopeAddress: String): List<AssetScopeAttribute>
//
//    /**
//     * Retrieves the FeePaymentDetail related to an asset by reference its asset uuid and the specific asset type to be
//     * verified.  This includes information for each entity that will receive payment when the verifier completes its
//     * verification.
//     * If no detail is present or an error occurs, null will be returned.
//     *
//     * @param assetUuid The asset uuid that correlates to the scope address.
//     * @param assetType The type of asset for which the scope is receiving verification.
//     * @param throwExceptions If an exception occurs in the smart contract or with the [PbClient][io.provenance.client.grpc.PbClient], it will be re-thrown
//     * unless this value is set to false.  An exception is normally thrown by the smart contract when the scope
//     * attribute is missing, but that exception will be ignored and null will be returned instead, regardless of this
//     * boolean's value.
//     */
//    fun queryFeePaymentsByAssetUuidOrNull(
//        assetUuid: UUID,
//        assetType: String,
//        throwExceptions: Boolean = false,
//    ): FeePaymentDetail?
//
//    /**
//     * Retrieves the FeePaymentDetail related to an asset by reference its asset uuid and the specific asset type to be
//     * verified.  This includes information for each entity that will receive payment when the verifier completes its
//     * verification.
//     * If no detail is present or an error occurs, an exception will be thrown.
//     *
//     * @param assetUuid The asset uuid that correlates to the scope address.
//     * @param assetType The type of asset for which the scope is receiving verification.
//     */
//    fun queryFeePaymentsByAssetUuid(
//        assetUuid: UUID,
//        assetType: String,
//    ): FeePaymentDetail
//
//    /**
//     * Retrieves the FeePaymentDetail related to an asset by directly referencing its scope address and the specific
//     * asset type to be verified.  This includes information for each entity that will receive payment when the verifier
//     * completes its verification.
//     * If no detail is present or an error occurs, null will be returned.
//     *
//     * @param scopeAddress The bech32 address assigned to the scope.  Begins with "scope"
//     * @param assetType The type of asset for which the scope is receiving verification.
//     * @param throwExceptions If an exception occurs in the smart contract or with the [PbClient][io.provenance.client.grpc.PbClient], it will be re-thrown
//     * unless this value is set to false.  An exception is normally thrown by the smart contract when the scope
//     * attribute is missing, but that exception will be ignored and null will be returned instead, regardless of this
//     * boolean's value.
//     */
//    fun queryFeePaymentsByScopeAddressOrNull(
//        scopeAddress: String,
//        assetType: String,
//        throwExceptions: Boolean = false,
//    ): FeePaymentDetail?
//
//    /**
//     * Retrieves the FeePaymentDetail related to an asset by directly referencing its scope address and the specific
//     * asset type to be verified.  This includes information for each entity that will receive payment when the verifier
//     * completes its verification.
//     * If no detail is present or an error occurs, an exception will be thrown.
//     *
//     * @param scopeAddress The bech32 address assigned to the scope.  Begins with "scope"
//     * @param assetType The type of asset for which the scope is receiving verification.
//     */
//    fun queryFeePaymentsByScopeAddress(
//        scopeAddress: String,
//        assetType: String,
//    ): FeePaymentDetail
//
//    /**
//     * Retrieves the base contract state for the current environment.  This response notably includes the admin address
//     * for the contract.
//     */
//    fun queryContractState(): VOContractState
//
//    /**
//     * Retrieves the contract versioning and name for the current environment.
//     */
//    fun queryContractVersion(): VOVersionInfo
}
