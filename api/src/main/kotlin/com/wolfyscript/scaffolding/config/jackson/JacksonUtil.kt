/*
 *       WolfyUtilities, APIs and Utilities for Minecraft Spigot plugins
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.wolfyscript.scaffolding.config.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.io.IOException

object JacksonUtil {
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
