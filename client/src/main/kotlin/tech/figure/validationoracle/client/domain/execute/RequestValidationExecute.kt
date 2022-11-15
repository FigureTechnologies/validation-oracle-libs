package tech.figure.validationoracle.client.domain.execute

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import cosmos.base.v1beta1.CoinOuterClass
import tech.figure.validationoracle.client.domain.execute.base.ContractExecute
import tech.figure.validationoracle.client.domain.serialization.RequestValidationExecuteSerializer

/**
 * This class is a reflection of the request body used in the validation oracle smart contract's request validation
 * execution route.
 *
 * To use it, simply create the execute class and call the appropriate function:
 * ```kotlin
 * val execute = RequestValidationExecute(validationType, displayName, validators, enabled = true)
 * val txResponse = VOClient.requestValidationExecute(execute, signer, options)
 * ```
 *
 * @param validationType A pretty human-readable name for this validation type.
 * @param displayName A pretty human-readable name for this validation type (vs a typically snake_case request_validation name).
 * @param validators All validators that support validating this specific validation type.
 * @param enabled Whether or not this validation type will accept incoming onboard requests.  If left null, the default value used will be `true`.  This parameter can only be changed by the contract owner.
 * @param bindName Whether or not to bind the name value creating an validation definition.
 */
@JsonSerialize(using = RequestValidationExecuteSerializer::class)
data class RequestValidationExecute(
    val id: String,
    val scopes: List<String> = emptyList(),
    val quote: List<CoinOuterClass.Coin> = emptyList(),
) : ContractExecute
