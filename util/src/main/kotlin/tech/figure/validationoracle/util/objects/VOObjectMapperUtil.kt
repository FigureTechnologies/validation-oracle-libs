package tech.figure.validationoracle.util.objects

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.paranamer.ParanamerModule
import com.hubspot.jackson.datatype.protobuf.ProtobufModule

object VOObjectMapperUtil { // TODO: Verify if this gets published
    fun getObjectMapper(): ObjectMapper = ObjectMapper().apply {
        registerKotlinModule()
        registerModule(ParanamerModule())
        registerModule(JavaTimeModule())
        registerModule(ProtobufModule())
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
}
