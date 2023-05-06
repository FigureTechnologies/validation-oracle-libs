package tech.figure.validationoracle.client.domain.execute

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import tech.figure.validationoracle.client.domain.execute.base.ContractExecuteInput
import tech.figure.validationoracle.client.domain.serialization.ContractSettingsUpdateSerializer

@JsonSerialize(using = ContractSettingsUpdateSerializer::class)
data class ContractSettingsUpdate(
    val newAdminAddress: String? = null,
) : ContractExecuteInput
