/*
 *       ____ _  _ ____ ___ ____ _  _ ____ ____ ____ ____ ___ _ _  _ ____
 *       |    |  | [__   |  |  | |\/| |    |__/ |__| |___  |  | |\ | | __
 *       |___ |__| ___]  |  |__| |  | |___ |  \ |  | |     |  | | \| |__]
 *
 *       CustomCrafting Recipe creation and management tool for Minecraft
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
package com.wolfyscript.scafall.spigot.api.nbt

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.InjectableValues
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import com.fasterxml.jackson.databind.node.ObjectNode
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.config.jackson.*
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Keyed
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.spigot.platform.nbtQueries
import de.tr7zw.changeme.nbtapi.NBTCompound
import de.tr7zw.changeme.nbtapi.NBTList
import de.tr7zw.changeme.nbtapi.NBTType
import java.io.IOException
import java.util.*

@JsonTypeResolver(KeyedTypeResolver::class)
@JsonTypeIdResolver(
    KeyedTypeIdResolver::class
)
@OptionalValueDeserializer(deserializer = QueryNode.OptionalValueDeserializer::class, delegateObjectDeserializer = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type", defaultImpl = QueryNodeCompound::class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder(value = ["type"])
abstract class QueryNode<VAL> protected constructor(
    @JsonIgnore @JacksonInject("key") protected val key: String,
    @JsonIgnore @JacksonInject("path") protected val parentPath: String?
) : Keyed {

    @get:JsonGetter
    protected val type: Key = Key.parse(StaticNamespacedKey.KeyBuilder.createKeyString(javaClass))

    @JsonIgnore
    var nbtType: NBTType = NBTType.NBTTagEnd
        protected set

    /**
     * Reads the targeted value at the specified key of a parent NBTCompound.<br></br>
     *
     * @param path   The path of the **parent** NBTCompound.
     * @param key    The key of child to read.
     * @param parent The parent NBTCompound to read the child from.
     * @return Optional value that is read from the parent.
     */
    protected abstract fun readValue(path: String?, key: String?, parent: NBTCompound): VAL?

    /**
     * Reads the targeted value at the specified index of a parent NBTList.<br></br>
     *
     * @param path   The path of the **parent** NBTList.
     * @param index  The index of child to read.
     * @param parent The parent NBTList to read the child from.
     * @return Optional value that is read from the parent.
     */
    protected fun readValue(path: String?, index: Int, parent: NBTList<VAL>): VAL? {
        if (index < parent.size) {
            return parent[index]
        }
        return null
    }

    abstract fun check(key: String?, nbtType: NBTType, context: EvalContext, value: VAL): Boolean

    /**
     * Applies the value to the specified key in the result NBTCompound.
     *
     * @param path            The path of the **parent** NBTCompound.
     * @param key             The key of child to apply the value for.
     * @param value           The available value read from the parent. (Read via [.readValue])
     * @param resultContainer The result NBTCompound to apply the value to. This compound is part of the container that will be returned once the query is completed.
     */
    protected abstract fun applyValue(
        path: String,
        key: String,
        context: EvalContext,
        value: VAL,
        resultContainer: NBTCompound
    )

    /**
     * Applies the value to result NBTList.
     *
     * @param path       The path of the **parent** NBTList.
     * @param index      The index of child to apply the value for.
     * @param value      The available value read from the parent. (Read via [.readValue])
     * @param resultList The result NBTList to apply the value to. This list is part of the container that will be returned once the query is completed.
     */
    protected fun applyValue(path: String?, index: Int, context: EvalContext?, value: VAL, resultList: NBTList<VAL>) {
        resultList.add(value)
    }

    fun visit(path: String, key: String, context: EvalContext, parent: NBTCompound, resultContainer: NBTCompound) {
        readValue(path, key, parent)?.apply {
            check(key, parent.getType(key), context, this)
        }?.let {
            applyValue(path, key, context, it, resultContainer)
        }
    }

    fun visit(path: String?, index: Int, context: EvalContext, parentList: NBTList<VAL>, resultList: NBTList<VAL>) {
        readValue(path, index, parentList)?.apply {
            check(key, parentList.type, context, this)
        }?.let {
            applyValue(path, index, context, it, resultList)
        }
    }

    @JsonIgnore
    override fun key(): Key {
        return type
    }

    abstract fun copy(): QueryNode<VAL>

    class OptionalValueDeserializer : ValueDeserializer<QueryNode<*>>(QueryNode::class.java) {

        @Throws(IOException::class)
        override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): QueryNode<*>? {
            if (jsonParser.isExpectedStartObjectToken) {
                return null
            }
            val token = jsonParser.currentToken()
            var node: JsonNode? = null
            val regNBTQueries = ScafallProvider.get().registries.nbtQueries
            val type: Key? = when (token) {
                JsonToken.VALUE_STRING -> {
                    node = jsonParser.readValueAsTree()
                    val text = node.asText()
                    when (if (!text.isBlank()) text[text.length - 1] else '0') {
                        'b', 'B' -> regNBTQueries.getKey(
                            QueryNodeByte::class.java
                        )

                        's', 'S' -> regNBTQueries.getKey(QueryNodeShort::class.java)
                        'i', 'I' -> regNBTQueries.getKey(QueryNodeInt::class.java)
                        'l', 'L' -> regNBTQueries.getKey(QueryNodeLong::class.java)
                        'f', 'F' -> regNBTQueries.getKey(QueryNodeFloat::class.java)
                        'd', 'D' -> regNBTQueries.getKey(QueryNodeDouble::class.java)
                        else -> regNBTQueries.getKey(QueryNodeString::class.java)
                    }
                }

                JsonToken.VALUE_NUMBER_INT -> regNBTQueries.getKey(QueryNodeInt::class.java)
                JsonToken.VALUE_NUMBER_FLOAT -> regNBTQueries.getKey(QueryNodeDouble::class.java)
                JsonToken.VALUE_FALSE, JsonToken.VALUE_TRUE -> regNBTQueries.getKey(
                    QueryNodeBoolean::class.java
                )

                else -> null
            }
            if (type == null) return null
            if (node == null) {
                node = jsonParser.readValueAsTree()
            }
            val objNode = ObjectNode(context.nodeFactory)
            objNode.put("type", type.toString())
            objNode.set<JsonNode>("value", node)
            return context.readTreeAsValue(objNode, QueryNode::class.java)
        }
    }

    companion object {
        private const val ERROR_MISMATCH = "Mismatched NBT types! Requested type: %s but found type %s, at node %s.%s"

        fun loadFrom(node: JsonNode?, parentPath: String?, key: String?): Optional<QueryNode<*>> {
            val injectVars = InjectableValues.Std()
            injectVars.addValue("key", key)
            injectVars.addValue("parent_path", parentPath)
            try {
                val queryNode: QueryNode<*> =
                    JacksonUtil.objectMapper.reader(injectVars).readValue(
                        node,
                        QueryNode::class.java
                    )
                return Optional.ofNullable(queryNode)
            } catch (e: IOException) {
                e.printStackTrace()
                return Optional.empty()
            }
        }
    }
}
