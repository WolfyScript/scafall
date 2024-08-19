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
package com.wolfyscript.scafall.spigot.platform.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.module.SimpleModule
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.config.jackson.JacksonUtil.addSerializerAndDeserializer
import org.bukkit.Color

object ColorSerialization {
    fun create(module: SimpleModule) {
        addSerializerAndDeserializer(module,
            Color::class.java,
            { value, gen, _ ->
                gen.writeStartObject()
                gen.writeNumberField("red", value.red)
                gen.writeNumberField("green", value.green)
                gen.writeNumberField("blue", value.blue)
                gen.writeEndObject()
            },
            { parser, _ ->
                val node = parser.readValueAsTree<JsonNode>()
                if (node.isObject) {
                    val red = node["red"].asInt()
                    val green = node["green"].asInt()
                    val blue = node["blue"].asInt()
                    return@addSerializerAndDeserializer Color.fromBGR(blue, green, red)
                }
                ScafallProvider.get().logger.warn("Error Deserializing Color! Invalid Color object!")
                return@addSerializerAndDeserializer null
            })
    }
}
