package tech.figure.validationoracle.util.internal

import com.fasterxml.jackson.databind.ObjectMapper
import tech.figure.validationoracle.util.objects.VOObjectMapperUtil

internal val OBJECT_MAPPER: ObjectMapper by lazy { VOObjectMapperUtil.getObjectMapper() }
