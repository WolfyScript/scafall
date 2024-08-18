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
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.wolfyscript.scaffolding.config.jackson.Deserialize
import com.wolfyscript.scaffolding.config.jackson.JacksonUtil.addSerializerAndDeserializer
import com.wolfyscript.scaffolding.config.jackson.Serialize
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.yaml.snakeyaml.Yaml

object ItemStackSerialization {
    fun create(module: SimpleModule) {
        addSerializerAndDeserializer(module,
            ItemStack::class.java,
            Serialize { itemStack: ItemStack?, gen: JsonGenerator, serializerProvider: SerializerProvider? ->
                if (itemStack != null) {
                    val yaml = Yaml()
                    val config = YamlConfiguration()
                    config["i"] = itemStack
                    val map = yaml.load<Map<String, Any>>(config.saveToString())
                    gen.writeObject(map["i"])
                }
            },
            Deserialize { p: JsonParser, deserializationContext: DeserializationContext? ->
                val node = p.readValueAsTree<JsonNode>()
                if (node.isValueNode) {
                    //Old Serialization Methods. like Base64 or NMS serialization
                    return@Deserialize null
                }
                val config = YamlConfiguration()
                //Loads the Map from the JsonNode && Sets the Map to YamlConfig
                config["i"] = p.codec.readValue(node.traverse(p.codec), object : TypeReference<Map<String, Any>>() {})
                try {
                    /*
                    Load new YamlConfig from just saved string.
                    That will convert the Map to an ItemStack!
                     */
                    config.loadFromString(config.saveToString())
                    return@Deserialize config.getItemStack("i")
                } catch (e: InvalidConfigurationException) {
                    e.printStackTrace()
                }
                null
            })
    }
}
