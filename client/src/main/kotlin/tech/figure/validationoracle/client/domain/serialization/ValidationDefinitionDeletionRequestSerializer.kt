package tech.figure.validationoracle.client.domain.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import tech.figure.validationoracle.client.domain.execute.ValidationDefinitionDeletionRequest

/**
 * The JSON structure of a contract entry point is impossible to represent with vanilla Jackson annotation setups. This
 * serializer enables the object to be serialized correctly with its multiple-nested nodes without enabling special
 * ObjectMapper features, allowing the object to be more universally applicable to external ObjectMapper singleton
 * instances.
 *
 * This serializer outputs the values in the following format:
 * ```json
 * {
 *      "delete_validation_definition": {
 *          "validation_type": ...
 *      }
 * }
 * ```
 */
class ValidationDefinitionDeletionRequestSerializer : JsonSerializer<ValidationDefinitionDeletionRequest>() {
    override fun serialize(value: ValidationDefinitionDeletionRequest, gen: JsonGenerator, provider: SerializerProvider?) {
        SafeJsonGenerator(gen).apply {
            jsonObject {
                jsonObject("delete_validation_definition") {
                    gen.writeStringField("validation_type", value.validationType)
                }
            }
        }
    }
}
