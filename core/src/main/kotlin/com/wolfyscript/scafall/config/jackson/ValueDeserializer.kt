package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import java.io.IOException

abstract class ValueDeserializer<T> protected constructor(var type: Class<T>) {
    @Throws(IOException::class)
    abstract fun deserialize(p: JsonParser, ctxt: DeserializationContext): T?
}
