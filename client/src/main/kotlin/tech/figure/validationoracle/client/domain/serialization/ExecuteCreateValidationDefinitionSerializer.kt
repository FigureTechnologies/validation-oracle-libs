package tech.figure.validationoracle.client.domain.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import tech.figure.validationoracle.client.domain.execute.CreateValidationDefinition

/**
 * The CreateValidationDefinition JSON structure is impossible to represent with vanilla Jackson annotation setups.  This
 * serializer enables the object to be serialized correctly with its multiple-nested nodes without enabling special
 * ObjectMapper features, allowing the object to be more universally applicable to external ObjectMapper singleton
 * instances.
 *
 * This serializer outputs the values in the following format:
 * ```json
 * {
 *      "create_validation_definition": {
 *          "request": {
 *              ...
 *          }
 *      }
 * }
 * ```
 */
class ExecuteCreateValidationDefinitionSerializer : JsonSerializer<CreateValidationDefinition>() {
    override fun serialize(value: CreateValidationDefinition, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeStartObject() // Start root node
        gen.writeObjectFieldStart("create_validation_definition") // Start create_validation_definition node
        gen.writeObjectFieldStart("request") // Start request node
        gen.writeStringField("validation_type", value.validationType)
        value.displayName?.let { displayName -> gen.writeStringField("display_name", displayName) }
        value.enabled?.let { enabled -> gen.writeBooleanField("enabled", enabled) }
        value.bindName?.let { bindName -> gen.writeBooleanField("bind_name", bindName) }
        gen.writeEndObject() // End request node
        gen.writeEndObject() // End create_validation_definition node
        gen.writeEndObject() // End root node
    }
}
