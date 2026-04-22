package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException

fun interface Serialize<T> {

    @Throws(IOException::class)
    fun serialize(t: T, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider)
}
