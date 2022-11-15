package tech.figure.validationoracle.client.domain.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import tech.figure.validationoracle.client.domain.execute.AddValidationDefinitionExecute

/**
 * The AddValidationDefinitionExecute JSON structure is impossible to represent with vanilla Jackson annotation setups.  This
 * serializer enables the object to be serialized correctly with its multiple-nested nodes without enabling special
 * ObjectMapper features, allowing the object to be more universally applicable to external ObjectMapper singleton
 * instances.
 *
 * This serializer outputs the values in the following format:
 * ```json
 * {
 *      "add_validaiton_definition": {
 *          "validaiton_definition": {
 *              ...
 *          }
 *      }
 * }
 * ```
 */
class AddValidationDefinitionExecuteSerializer : JsonSerializer<AddValidationDefinitionExecute>() {
    override fun serialize(value: AddValidationDefinitionExecute, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeStartObject() // Start add_validation_definition node
        gen.writeObjectFieldStart("create_validation_definition") // Start create_validation_definition node
        gen.writeObjectFieldStart("request") // Start request node
        gen.writeStringField("validation_type", value.validationType)
        value.displayName?.also { displayName -> gen.writeStringField("display_name", displayName) }
        gen.writeArrayFieldStart("validators")
        value.validators.forEach { validator -> gen.writeObject(validator) }
        gen.writeEndArray()
        value.enabled?.also { enabled -> gen.writeBooleanField("enabled", enabled) }
        value.bindName?.also { bindName -> gen.writeBooleanField("bind_name", bindName) }
        gen.writeEndObject() // End request node
        gen.writeEndObject() // End create_validation_definition node
        gen.writeEndObject() // End root node
    }
}
