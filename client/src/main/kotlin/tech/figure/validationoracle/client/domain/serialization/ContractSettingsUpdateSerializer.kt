package tech.figure.validationoracle.client.domain.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import tech.figure.validationoracle.client.domain.execute.ContractSettingsUpdate

/**
 * The JSON structure of a contract entry point is impossible to represent with vanilla Jackson annotation setups. This
 * serializer enables the object to be serialized correctly with its multiple-nested nodes without enabling special
 * ObjectMapper features, allowing the object to be more universally applicable to external ObjectMapper singleton
 * instances.
 *
 * This serializer outputs the values in the following format:
 * ```json
 * {
 *      "update_settings": {
 *          "new_admin_address": ...
 *      }
 * }
 * ```
 */
class ContractSettingsUpdateSerializer : JsonSerializer<ContractSettingsUpdate>() {
    override fun serialize(value: ContractSettingsUpdate, gen: JsonGenerator, provider: SerializerProvider?) {
        SafeJsonGenerator(gen).apply {
            jsonObject {
                jsonObject("update_settings") {
                    jsonObject("update") {
                        gen.writeStringField("new_admin_address", value.newAdminAddress)
                    }
                }
            }
        }
    }
}
