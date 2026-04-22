package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.wolfyscript.scafall.ScafallProvider
import java.io.IOException

interface JacksonUtil {

    /**
     * The scafall module contains everything needed to deserialize/serialize scafall api types.
     *
     * It may contain but is not limited to abstract type mappings from api to implementation, custom serializers and deserializers.
     */
    fun registerScafallModule(mapper: ObjectMapper): ObjectMapper

    companion object {

        val objectMapper: ObjectMapper = ObjectMapper()

        @JvmStatic
        fun getObjectWriter(prettyPrinting: Boolean): ObjectWriter {
            return objectMapper.writer(if (prettyPrinting) DefaultPrettyPrinter() else null)
        }

        @JvmStatic
        fun registerModule(module: Module?) {
            objectMapper.registerModule(module)
        }

        @JvmStatic
        fun <T> addSerializer(module: SimpleModule, type: Class<T>?, serialize: Serialize<T>) {
            module.addSerializer(type, object : StdSerializer<T>(type) {
                @Throws(IOException::class)
                override fun serialize(t: T, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
                    serialize.serialize(t, jsonGenerator, serializerProvider)
                }
            })
        }

        @JvmStatic
        fun <T> addDeserializer(module: SimpleModule, type: Class<T>, deserialize: Deserialize<T>) {
            module.addDeserializer(type, object : StdDeserializer<T>(type) {
                @Throws(IOException::class)
                override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): T? {
                    return deserialize.deserialize(jsonParser, deserializationContext)
                }
            })
        }

        @JvmStatic
        fun <T> addSerializerAndDeserializer(
            module: SimpleModule,
            t: Class<T>,
            serialize: Serialize<T>,
            deserialize: Deserialize<T>
        ) {
            addSerializer(module, t, serialize)
            addDeserializer(module, t, deserialize)
        }
    }
}

fun ObjectMapper.registerScafallModule(): ObjectMapper = ScafallProvider.get().jacksonUtil.registerScafallModule(this)
