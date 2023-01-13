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
        SafeJsonGenerator(gen).apply {
            jsonObject {
                jsonObject("update_validation_request") {
                    jsonObject("request") {
                        gen.writeStringField("current_id", value.currentId)
                        value.newId?.let { newId -> gen.writeStringField("new_id", newId) }
                        value.newScopes?.let { newScopes ->
                            jsonArray("new_scopes") {
                                newScopes.forEach { newScope ->
                                    gen.writeObject(newScope) // TODO: Should this use writeString instead?
                                }
                            }
                        }
                        value.newAllowedValidators?.let { newAllowedValidators ->
                            jsonArray("new_allowed_validators") {
                                newAllowedValidators.forEach { newAllowedValidator ->
                                    gen.writeObject(newAllowedValidator) // TODO: Should this use writeString instead?
                                }
                            }
                        }
                        value.newQuote?.let { newQuote ->
                            jsonArray("new_quote") {
                                newQuote.forEach { coin ->
                                    gen.writeObject(coin) // TODO: Should this use writeString instead?
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
