package tech.figure.validationoracle.client.domain.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import tech.figure.validationoracle.client.domain.execute.ValidationRequestUpdate

/**
 * The JSON structure of a contract entry point is impossible to represent with vanilla Jackson annotation setups. This
 * serializer enables the object to be serialized correctly with its multiple-nested nodes without enabling special
 * ObjectMapper features, allowing the object to be more universally applicable to external ObjectMapper singleton
 * instances.
 *
 * This serializer outputs the values in the following format:
 * ```json
 * {
 *      "update_validation_request": {
 *          "request": {
 *              ...
 *          }
 *      }
 * }
 * ```
 */
class ValidationRequestUpdateSerializer : JsonSerializer<ValidationRequestUpdate>() {
    override fun serialize(value: ValidationRequestUpdate, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeStartObject() // Start root node
        gen.writeObjectFieldStart("update_validation_request") // Start update_validation_request node
        gen.writeObjectFieldStart("request") // Start request node
        gen.writeStringField("current_id", value.currentId)
        value.newId?.let { newId -> gen.writeStringField("new_id", newId) }
        value.newScopes?.let { newScopes ->
            gen.writeArrayFieldStart("new_scopes")
            newScopes.forEach { newScope ->
                gen.writeObject(newScope) // TODO: Should this use writeString instead?
            }
            gen.writeEndArray()
        }
        value.newAllowedValidators?.let { newAllowedValidators ->
            gen.writeArrayFieldStart("new_allowed_validators")
            newAllowedValidators.forEach { newAllowedValidator ->
                gen.writeObject(newAllowedValidator) // TODO: Should this use writeString instead?
            }
            gen.writeEndArray()
        }
        value.newQuote?.let { newQuote ->
            gen.writeArrayFieldStart("new_quote")
            newQuote.forEach { coin ->
                gen.writeObject(coin) // TODO: Should this use writeString instead?
            }
            gen.writeEndArray()
        }
        gen.writeEndObject() // End request node
        gen.writeEndObject() // End update_validation_request node
        gen.writeEndObject() // End root node
    }
}
