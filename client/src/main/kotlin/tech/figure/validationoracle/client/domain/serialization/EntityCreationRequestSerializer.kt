package tech.figure.validationoracle.client.domain.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import tech.figure.validationoracle.client.domain.execute.EntityCreationRequest

/**
 * The JSON structure of a contract entry point is impossible to represent with vanilla Jackson annotation setups. This
 * serializer enables the object to be serialized correctly with its multiple-nested nodes without enabling special
 * ObjectMapper features, allowing the object to be more universally applicable to external ObjectMapper singleton
 * instances.
 *
 * This serializer outputs the values in the following format:
 * ```json
 * {
 *      "create_entity": {
 *          "entity": {
 *              ...
 *          }
 *      }
 * }
 * ```
 */
class EntityCreationRequestSerializer : JsonSerializer<EntityCreationRequest>() {
    override fun serialize(value: EntityCreationRequest, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeStartObject() // Start root node
        gen.writeObjectFieldStart("create_entity") // Start create_entity node
        gen.writeObjectField("entity", value.entity)
        gen.writeEndObject() // End create_entity node
        gen.writeEndObject() // End root node
    }
}
