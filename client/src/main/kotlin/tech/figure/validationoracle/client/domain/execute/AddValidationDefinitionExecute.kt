package tech.figure.validationoracle.client.domain.execute

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import tech.figure.validationoracle.client.domain.model.ValidatorValidationConfiguration
import tech.figure.validationoracle.client.domain.execute.base.ContractExecute
import tech.figure.validationoracle.client.domain.serialization.AddValidationDefinitionExecuteSerializer

/**
 * This class is a reflection of the request body used in the validation oracle smart contract's add asset
 * definition execution route.
 *
 * To use it, simply create the execute class and call the appropriate function:
 * ```kotlin
 * val execute = AddAssetDefinitionExecute(assetType, displayName, verifiers, enabled = true)
 * val txResponse = acClient.addAssetDefinition(execute, signer, options)
 * ```
 *
 * @param assetType The type of asset that will be added. This value is a unique key in the contract.
 * @param displayName A pretty human-readable name for this asset type (vs a typically snake_case asset_type name).
 * @param verifiers All verifiers that are allowed to do verification for this specific asset type.
 * @param enabled Whether or not this asset type will accept incoming onboard requests.  If left null, the default value used will be `true`
 * @param bindName Whether or not to bind the name value creating an asset definition.
 */
@JsonSerialize(using = AddValidationDefinitionExecuteSerializer::class)
data class AddValidationDefinitionExecute(
    val assetType: String,
    val validationType: String,
    val displayName: String? = null,
    val verifiers: List<ValidatorValidationConfiguration>,
    val enabled: Boolean? = null,
    val bindName: Boolean? = null,
) : ContractExecute
