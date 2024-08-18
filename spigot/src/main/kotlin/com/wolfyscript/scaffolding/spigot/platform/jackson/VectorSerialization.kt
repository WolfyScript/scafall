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
package com.wolfyscript.scaffolding.spigot.platform.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ArrayNode
import com.wolfyscript.scaffolding.ScaffoldingProvider
import com.wolfyscript.scaffolding.config.jackson.Deserialize
import com.wolfyscript.scaffolding.config.jackson.JacksonUtil.addSerializerAndDeserializer
import com.wolfyscript.scaffolding.config.jackson.Serialize
import org.bukkit.util.Vector

object VectorSerialization {
    fun create(module: SimpleModule) {
        addSerializerAndDeserializer<Vector>(module,
            Vector::class.java,
            Serialize<Vector> { value: Vector, gen: JsonGenerator, serializerProvider: SerializerProvider? ->
                gen.writeStartArray()
                gen.writeNumber(value.x)
                gen.writeNumber(value.y)
                gen.writeNumber(value.z)
                gen.writeEndArray()
            },
            Deserialize<Vector> { p: JsonParser, deserializationContext: DeserializationContext? ->
                val node = p.readValueAsTree<JsonNode>()
                if (node.isArray) {
                    val arrayNode = node as ArrayNode
                    return@Deserialize Vector(
                        arrayNode[0].asDouble(0.0),
                        arrayNode[1].asDouble(0.0),
                        arrayNode[2].asDouble(0.0)
                    )
                }
                ScaffoldingProvider.get().logger.warn("Error Deserializing Vector! Invalid Vector object!")
                null
            })
    }
}
