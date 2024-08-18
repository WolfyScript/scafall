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
package com.wolfyscript.scaffolding.nbt

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import com.fasterxml.jackson.databind.node.ObjectNode
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.ScaffoldingProvider.Companion.get
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeIdResolver
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeResolver
import com.wolfyscript.scaffolding.config.jackson.OptionalValueDeserializer
import com.wolfyscript.scaffolding.config.jackson.ValueDeserializer
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.parse
import com.wolfyscript.scaffolding.identifier.Keyed
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey.KeyBuilder.createKeyString
import com.wolfyscript.scaffolding.nbt.NBTTagConfigBoolean
import com.wolfyscript.scaffolding.nbt.NBTTagConfigByte
import com.wolfyscript.scaffolding.nbt.NBTTagConfigDouble
import com.wolfyscript.scaffolding.nbt.NBTTagConfigFloat
import com.wolfyscript.scaffolding.nbt.NBTTagConfigInt
import com.wolfyscript.scaffolding.nbt.NBTTagConfigLong
import com.wolfyscript.scaffolding.nbt.NBTTagConfigShort
import com.wolfyscript.scaffolding.nbt.NBTTagConfigString
import java.io.IOException
import java.util.regex.Pattern

@JsonTypeResolver(KeyedTypeResolver::class)
@JsonTypeIdResolver(
    KeyedTypeIdResolver::class
)
@OptionalValueDeserializer(
    deserializer = NBTTagConfig.OptionalValueDeserializer::class,
    delegateObjectDeserializer = true
)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type", defaultImpl = NBTTagConfigCompound::class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder(value = ["type"])
abstract class NBTTagConfig : Keyed {
    @JsonIgnore
    protected val wolfyUtils: Scaffolding

    @JsonIgnore
    val type: Key

    @get:JsonIgnore
    @set:JsonIgnore
    @JsonIgnore
    var parent: NBTTagConfig? = null

    protected constructor() {
        this.wolfyUtils = get()
        this.type = parse(createKeyString(javaClass))
    }

    protected constructor(parent: NBTTagConfig?) {
        this.wolfyUtils = get()
        this.type = parse(createKeyString(javaClass))
        this.parent = parent
    }

    @JsonIgnore
    override fun key(): Key = type

    abstract fun copy(): NBTTagConfig

    class OptionalValueDeserializer : ValueDeserializer<NBTTagConfig>(NBTTagConfig::class.java) {

        @Throws(IOException::class)
        override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext): NBTTagConfig? {
            if (jsonParser.isExpectedStartObjectToken) {
                return null
            }
            val scaffolding = get()
            val token = jsonParser.currentToken()
            var node: JsonNode? = null
            val regNBTQueries = scaffolding.registries.nbtTagConfigs
            val type = when (token) {
                JsonToken.VALUE_STRING -> {
                    node = jsonParser.readValueAsTree()
                    val text = node.asText()
                    val matcher = NUM_PATTERN.matcher(text)
                    if (matcher.matches()) {
                        var id = matcher.group(2)
                        if (id != null) {
                            // integer value
                        } else {
                            // float value
                            id = matcher.group(4)
                        }
                        when (id!![0]) {
                            'b', 'B' -> regNBTQueries.getKey(NBTTagConfigByte::class.java)
                            's', 'S' -> regNBTQueries.getKey(NBTTagConfigShort::class.java)
                            'i', 'I' -> regNBTQueries.getKey(NBTTagConfigInt::class.java)
                            'l', 'L' -> regNBTQueries.getKey(NBTTagConfigLong::class.java)
                            'f', 'F' -> regNBTQueries.getKey(NBTTagConfigFloat::class.java)
                            'd', 'D' -> regNBTQueries.getKey(NBTTagConfigDouble::class.java)
                            else -> regNBTQueries.getKey(NBTTagConfigString::class.java)
                        }
                    }
                    regNBTQueries.getKey(NBTTagConfigString::class.java)
                }

                JsonToken.VALUE_NUMBER_INT -> regNBTQueries.getKey(NBTTagConfigInt::class.java)
                JsonToken.VALUE_NUMBER_FLOAT -> regNBTQueries.getKey(NBTTagConfigDouble::class.java)
                JsonToken.VALUE_FALSE, JsonToken.VALUE_TRUE -> regNBTQueries.getKey(
                    NBTTagConfigBoolean::class.java
                )

                else -> null
            }
            if (type == null) return null
            if (node == null) {
                node = jsonParser.readValueAsTree()
            }
            val objNode = ObjectNode(ctxt.nodeFactory)
            objNode.put("type", type.toString())
            objNode.set<JsonNode>("value", node)
            return ctxt.readTreeAsValue(objNode, NBTTagConfig::class.java)
        }

        companion object {
            private val NUM_PATTERN: Pattern = Pattern.compile("([0-9]+)([bBsSiIlL])|([0-9]?\\.?[0-9])+([fFdD])")
        }
    }

    companion object {
        private const val ERROR_MISMATCH = "Mismatched NBT types! Requested type: %s but found type %s, at node %s.%s"
    }
}
