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
import com.wolfyscript.scafall.config.jackson.Deserialize
import com.wolfyscript.scafall.config.jackson.JacksonUtil.addSerializerAndDeserializer
import com.wolfyscript.scafall.config.jackson.Serialize
import org.bukkit.Color
import org.bukkit.Particle

object DustOptionsSerialization {
    fun create(module: SimpleModule) {
        addSerializerAndDeserializer(module,
            Particle.DustOptions::class.java,
            Serialize { dustOptions, gen, s ->
                gen.writeStartObject()
                gen.writeNumberField("size", dustOptions.size)
                gen.writeObjectField("color", dustOptions.color)
                gen.writeEndObject()
            },
            Deserialize { parser, _ ->
                val node = parser.readValueAsTree<JsonNode>()
                if (node.isObject) {
                    val size = node["size"].floatValue()
                    val color = parser.codec.treeToValue(node["color"], Color::class.java)
                    return@Deserialize Particle.DustOptions(color, size)
                }
                ScafallProvider.get().logger.warn("Error Deserializing DustOptions! Invalid DustOptions object!")
                null
            })
    }
}
