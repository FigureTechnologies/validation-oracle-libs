package tech.figure.validationoracle.client.domain.execute

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import tech.figure.validationoracle.client.domain.execute.base.ContractExecuteInput
import tech.figure.validationoracle.client.domain.serialization.ValidationDefinitionDeletionRequestSerializer

/**
 * This class is a reflection of the request body used in the validation oracle smart contract's execution route for
 * deleting a validation definition.
 *
 * @param validationType The type of validation denoting the definition.
 */
@JsonSerialize(using = ValidationDefinitionDeletionRequestSerializer::class)
data class ValidationDefinitionDeletionRequest(
    val validationType: String,
) : ContractExecuteInput
