package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import java.io.IOException

fun interface Deserialize<T> {

    @Throws(IOException::class)
    fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): T?
}
