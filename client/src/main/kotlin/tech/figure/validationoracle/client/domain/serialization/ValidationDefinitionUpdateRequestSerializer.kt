package tech.figure.validationoracle.client.domain.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import tech.figure.validationoracle.client.domain.execute.ValidationDefinitionUpdateRequest

/**
 * The JSON structure of a contract entry point is impossible to represent with vanilla Jackson annotation setups. This
 * serializer enables the object to be serialized correctly with its multiple-nested nodes without enabling special
 * ObjectMapper features, allowing the object to be more universally applicable to external ObjectMapper singleton
 * instances.
 *
 * This serializer outputs the values in the following format:
 * ```json
 * {
 *      "update_validation_definition": {
 *          "request": {
 *              ...
 *          }
 *      }
 * }
 * ```
 */
class ValidationDefinitionUpdateRequestSerializer : JsonSerializer<ValidationDefinitionUpdateRequest>() {
    override fun serialize(value: ValidationDefinitionUpdateRequest, gen: JsonGenerator, provider: SerializerProvider?) {
        SafeJsonGenerator(gen).apply {
            jsonObject {
                jsonObject("update_validation_definition") {
                    jsonObject("request") {
                        gen.writeStringField("current_validation_type", value.currentValidationType)
                        value.newValidationType?.let { newValidationType ->
                            gen.writeStringField("new_validation_type", newValidationType)
                        }
                        value.newDisplayName?.let { newDisplayName ->
                            gen.writeStringField("new_display_name", newDisplayName)
                        }
                        value.enabled?.let { enabled -> gen.writeBooleanField("enabled", enabled) }
                    }
                }
            }
        }
    }
}
