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
import com.wolfyscript.scaffolding.ScaffoldingProvider
import com.wolfyscript.scaffolding.config.jackson.Deserialize
import com.wolfyscript.scaffolding.config.jackson.JacksonUtil.addSerializerAndDeserializer
import com.wolfyscript.scaffolding.config.jackson.Serialize
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import java.util.*

object LocationSerialization {
    fun create(module: SimpleModule) {
        addSerializerAndDeserializer(module,
            Location::class.java,
            Serialize { location: Location, gen: JsonGenerator, serializerProvider: SerializerProvider? ->
                gen.writeStartObject()
                gen.writeStringField("world", location.world.uid.toString())
                gen.writeArrayFieldStart("pos")
                gen.writeNumber(location.x)
                gen.writeNumber(location.y)
                gen.writeNumber(location.z)
                gen.writeNumber(location.yaw)
                gen.writeNumber(location.pitch)
                gen.writeEndArray()
                gen.writeEndObject()
            },
            Deserialize { p: JsonParser, d: DeserializationContext? ->
                val node = p.readValueAsTree<JsonNode>()
                val api = ScaffoldingProvider.get()
                if (node.isObject) {
                    val uuid = UUID.fromString(node["world"].asText())
                    val world: World? = Bukkit.getWorld(uuid)
                    if (world != null) {
                        val jsonNode = node["pos"]
                        if (jsonNode.size() == 5) {
                            val x = jsonNode[0].asDouble()
                            val y = jsonNode[1].asDouble()
                            val z = jsonNode[2].asDouble()
                            val yaw = jsonNode[3].floatValue()
                            val pitch = jsonNode[4].floatValue()
                            return@Deserialize Location(world, x, y, z, yaw, pitch)
                        }
                        api.logger.warn("Error Deserializing Location! Invalid Position: expected array size 5 got " + jsonNode.size())
                        return@Deserialize null
                    }
                    api.logger.warn("Error Deserializing Location! Missing World with uid $uuid")
                    return@Deserialize null
                }
                api.logger.warn("Error Deserializing Location! Invalid Location object!")
                null
            })
    }
}
