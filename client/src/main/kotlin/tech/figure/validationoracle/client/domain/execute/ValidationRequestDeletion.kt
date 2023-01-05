package tech.figure.validationoracle.client.domain.execute

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import tech.figure.validationoracle.client.domain.execute.base.ContractExecuteInput
import tech.figure.validationoracle.client.domain.serialization.ValidationRequestDeletionSerializer

/**
 * This class is a reflection of the request body used in the validation oracle smart contract's execution route for
 * deleting a validation request.
 *
 * @param id The ID of the validation request.
 */
@JsonSerialize(using = ValidationRequestDeletionSerializer::class)
data class ValidationRequestDeletion(
    val id: String,
) : ContractExecuteInput
