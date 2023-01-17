package tech.figure.validationoracle.client.domain.serialization

import com.fasterxml.jackson.core.JsonGenerator

class SafeJsonGenerator(
    val generator: JsonGenerator,
) {
    fun jsonObject(fn: SafeJsonGenerator.() -> Unit) {
        generator.writeStartObject()
        fn()
        generator.writeEndObject()
    }
    fun jsonObject(key: String, fn: SafeJsonGenerator.() -> Unit) {
        generator.writeObjectFieldStart(key)
        fn()
        generator.writeEndObject()
    }
    fun jsonArray(key: String, fn: SafeJsonGenerator.() -> Unit) {
        generator.writeArrayFieldStart(key)
        fn()
        generator.writeEndArray()
    }
}
