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

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.wolfyscript.scafall.config.jackson.Deserialize
import com.wolfyscript.scafall.config.jackson.JacksonUtil.addSerializerAndDeserializer
import com.wolfyscript.scafall.config.jackson.Serialize
import org.bukkit.potion.PotionEffectType

object PotionEffectTypeSerialization {
    fun create(module: SimpleModule) {
        addSerializerAndDeserializer(module,
            PotionEffectType::class.java,
            Serialize { potionEffectType: PotionEffectType, gen: JsonGenerator, serializerProvider: SerializerProvider? ->
                gen.writeString(potionEffectType.name)
            },
            Deserialize { p: JsonParser, deserializationContext: DeserializationContext? ->
                val node = p.readValueAsTree<JsonNode>()
                PotionEffectType.getByName(node.asText())
            })
    }
}
