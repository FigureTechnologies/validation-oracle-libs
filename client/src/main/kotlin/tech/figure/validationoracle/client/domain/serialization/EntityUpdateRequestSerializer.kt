package tech.figure.validationoracle.client.domain.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import tech.figure.validationoracle.client.domain.execute.EntityUpdateRequest

/**
 * The JSON structure of a contract entry point is impossible to represent with vanilla Jackson annotation setups. This
 * serializer enables the object to be serialized correctly with its multiple-nested nodes without enabling special
 * ObjectMapper features, allowing the object to be more universally applicable to external ObjectMapper singleton
 * instances.
 *
 * This serializer outputs the values in the following format:
 * ```json
 * {
 *      "update_entity": {
 *          "entity": {
 *              ...
 *          }
 *      }
 * }
 * ```
 */
class EntityUpdateRequestSerializer : JsonSerializer<EntityUpdateRequest>() {
    override fun serialize(value: EntityUpdateRequest, gen: JsonGenerator, provider: SerializerProvider?) {
        SafeJsonGenerator(gen).apply {
            jsonObject {
                jsonObject("update_entity") {
                    gen.writeObjectField("entity", value.entity)
                }
            }
        }
    }
}
