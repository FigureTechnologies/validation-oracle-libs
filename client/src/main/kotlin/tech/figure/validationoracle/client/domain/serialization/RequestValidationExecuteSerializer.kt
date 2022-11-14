package tech.figure.validationoracle.client.domain.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import tech.figure.validationoracle.client.domain.execute.RequestValidationExecute

/**
 * The RequestValidationExecute JSON structure is impossible to represent with vanilla Jackson annotation setups.  This
 * serializer enables the object to be serialized correctly with its multiple-nested nodes without enabling special
 * ObjectMapper features, allowing the object to be more universally applicable to external ObjectMapper singleton
 * instances.
 *
 * This serializer outputs the values in the following format:
 * ```json
 * {
 *      "request_validation": {
 *          "request": {
 *              ...
 *          }
 *      }
 * }
 * ```
 */
class RequestValidationExecuteSerializer : JsonSerializer<RequestValidationExecute>() {
    override fun serialize(value: RequestValidationExecute, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeStartObject() // Start add_validation_definition node
        gen.writeObjectFieldStart("request_validation") // Start request_validation node
        gen.writeObjectFieldStart("request") // Start request node
        gen.writeStringField("id", value.id)
        gen.writeArrayFieldStart("scopes")
        value.scopes.forEach { scope -> gen.writeObject(scope) }
        gen.writeEndArray()
        gen.writeArrayFieldStart("quote")
        value.quote.forEach { aquote -> gen.writeObject(aquote) }
        gen.writeEndArray()
        gen.writeEndObject() // End request node
        gen.writeEndObject() // End request_validation node
        gen.writeEndObject() // End root node
    }
}
