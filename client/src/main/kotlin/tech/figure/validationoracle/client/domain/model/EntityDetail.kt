package tech.figure.validationoracle.client.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * The address of this entity accompanied by an optional set of fields that define
 *      the entity associated with the validation oracle smart contract.
 *
 * @param address The bech32 address of this entity.
 * @param name A short name describing the entity.
 * @param description A short description of the entity's purpose.
 * @param homeUrl A web link that can send observers to the organization that the verifier belongs to.
 * @param sourceUrl A web link that can send observers to the source code of the verifier, for increased transparency.
 */
@JsonNaming(SnakeCaseStrategy::class)
data class EntityDetail(
    val address: String,
    val name: String?,
    val description: String?,
    val homeUrl: String?,
    val sourceUrl: String?,
)
