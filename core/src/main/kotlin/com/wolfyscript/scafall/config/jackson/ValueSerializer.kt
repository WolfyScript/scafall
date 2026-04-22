package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException

abstract class ValueSerializer<T> protected constructor(var type: Class<T>) {
    @Throws(IOException::class)
    abstract fun serialize(targetObject: T, generator: JsonGenerator, provider: SerializerProvider): Boolean
}
