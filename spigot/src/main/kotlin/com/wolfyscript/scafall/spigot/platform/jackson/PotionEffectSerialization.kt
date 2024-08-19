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
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object PotionEffectSerialization {
    private const val AMPLIFIER = "amplifier"
    private const val DURATION = "duration"
    private const val TYPE = "effect"
    private const val AMBIENT = "ambient"
    private const val PARTICLES = "has-particles"
    private const val ICON = "has-icon"

    fun create(module: SimpleModule) {
        addSerializerAndDeserializer(module,
            PotionEffect::class.java,
            Serialize { potionEffect: PotionEffect, gen: JsonGenerator, serializerProvider: SerializerProvider? ->
                gen.writeStartObject()
                gen.writeNumberField(AMPLIFIER, potionEffect.amplifier)
                gen.writeNumberField(DURATION, potionEffect.duration)
                gen.writeObjectField(TYPE, potionEffect.type)
                gen.writeBooleanField(AMBIENT, potionEffect.isAmbient)
                gen.writeBooleanField(PARTICLES, potionEffect.hasParticles())
                gen.writeBooleanField(ICON, potionEffect.hasIcon())
                gen.writeEndObject()
            },
            Deserialize { p: JsonParser, deserializationContext: DeserializationContext? ->
                val node = p.readValueAsTree<JsonNode>()
                if (!node.has(TYPE)) return@Deserialize null
                val type = p.codec.treeToValue(
                    node.path(TYPE),
                    PotionEffectType::class.java
                )
                val particles = node.path(PARTICLES).asBoolean(true)
                val icon = node.path(ICON).asBoolean(particles)
                if (type == null) null else PotionEffect(
                    type, node.path(DURATION).asInt(), node.path(AMPLIFIER).asInt(), node.path(
                        AMBIENT
                    ).asBoolean(false), particles, icon
                )
            })
    }
}
