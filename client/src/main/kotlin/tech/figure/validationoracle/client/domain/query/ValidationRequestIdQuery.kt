package tech.figure.validationoracle.client.domain.query

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import tech.figure.validationoracle.client.domain.query.base.ContractQueryInput

/**
 * This class is a reflection of the request body used in the validation oracle smart contract's query validation
 * request route.
 * It is internally utilized in the [VOQuerier][tech.figure.validationoracle.client.client.base.VOQuerier].
 */
@JsonNaming(SnakeCaseStrategy::class)
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName("query_validation_request_by_id")
data class ValidationRequestIdQuery(val id: String) : ContractQueryInput {
    @JsonIgnore
    override val queryDescription: String = "query for validation request with ID $id"
}
