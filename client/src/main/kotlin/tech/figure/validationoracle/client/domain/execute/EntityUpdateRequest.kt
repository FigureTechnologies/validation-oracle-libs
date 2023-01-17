package tech.figure.validationoracle.client.domain.execute

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import tech.figure.validationoracle.client.domain.execute.base.ContractExecuteInput
import tech.figure.validationoracle.client.domain.model.EntityDetail
import tech.figure.validationoracle.client.domain.serialization.EntityUpdateRequestSerializer

@JsonSerialize(using = EntityUpdateRequestSerializer::class)
data class EntityUpdateRequest(
    val entity: EntityDetail,
) : ContractExecuteInput
