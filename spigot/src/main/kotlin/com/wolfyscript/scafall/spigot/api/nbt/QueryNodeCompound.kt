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
import com.fasterxml.jackson.databind.JsonNode
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.operator.BoolOperatorConst
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import de.tr7zw.nbtapi.NBTCompound
import de.tr7zw.nbtapi.NBTType
import java.util.*
import java.util.stream.Collectors

@StaticNamespacedKey(key = "compound")
open class QueryNodeCompound : QueryNode<NBTCompound> {
    @get:JsonGetter("preservePath")
    @set:JsonSetter("preservePath")
    var isPreservePath: Boolean = true

    //If include is true it includes this node with each and every child node.
    var isIncludeAll: Boolean = false

    //If includes has values it includes this node with the specified child nodes.
    @get:JsonGetter
    @set:JsonSetter
    var includes: Map<String, Boolean>

    //Checks and verifies the child nodes. This node is only included if all the child nodes are valid.
    @get:JsonGetter
    @set:JsonSetter
    var required: Map<String, QueryNode<*>?>

    //Child nodes to proceed to next. This is useful for further child compound tag settings.
    @JsonIgnore
    @get:JsonGetter
    protected var children: MutableMap<String, QueryNode<*>>

    constructor(
        @JacksonInject("key") key: String,
        @JacksonInject("parent_path") parentPath: String?
    ) : super(key, parentPath) {
        this.nbtType = NBTType.NBTTagCompound
        this.includes = HashMap()
        this.required = HashMap()
        this.children = HashMap()
    }

    protected constructor(other: QueryNodeCompound) : super(other.key, other.parentPath) {
        this.nbtType = NBTType.NBTTagCompound
        this.includes = HashMap(other.includes)
        this.isPreservePath = other.isPreservePath
        this.isIncludeAll = other.isIncludeAll
        this.required = other.required.entries.stream().collect(
            Collectors.toMap(
                { it.key },
                { entry -> entry.value!!.copy() })
        )
        this.children = other.children.entries.stream().collect(
            Collectors.toMap(
                { it.key },
                { entry -> entry.value.copy() })
        )
    }

    @JsonAnySetter
    open fun loadNonNestedChildren(key: String, node: JsonNode?) {
        //Sets the children that are specified in the root of the object without the "children" node!
        //That is supported behaviour!
        loadFrom(node, parentPath + "." + this.key, key).ifPresent { queryNode: QueryNode<*> -> children.putIfAbsent(key, queryNode) }
    }

    @JsonSetter("children")
    private fun jsonSetChildren(children: Map<String, JsonNode>) {
        this.children = children.entries.stream().map { entry ->
            loadFrom(entry.value, parentPath + "." + this.key, entry.key).map { queryNode ->
                java.util.Map.entry(entry.key, queryNode)
            }.orElse(null)
        }.filter { obj -> Objects.nonNull(obj) }.collect(Collectors.toMap({ it.key }, { it.value }))
    }

    override fun check(key: String?, nbtType: NBTType, context: EvalContext, value: NBTCompound): Boolean {
        return value.keys.isNotEmpty()
    }

    override fun readValue(path: String?, key: String?, parent: NBTCompound): NBTCompound? {
        return parent.getCompound(key)
    }

    public override fun applyValue(
        path: String,
        key: String,
        context: EvalContext,
        value: NBTCompound,
        resultContainer: NBTCompound
    ) {
        val newPath = "$path.$key"
        val container = if (isPreservePath) resultContainer.addCompound(key) else resultContainer
        applyChildrenToCompound(newPath, context, value, container)
    }

    /**
     *
     *
     * @param containerPath The path of the current container.
     * @param value The value of the NBTCompound at the current path.
     * @param resultContainer The current container to apply the children to.
     */
    protected fun applyChildrenToCompound(
        containerPath: String?,
        context: EvalContext,
        value: NBTCompound,
        resultContainer: NBTCompound?
    ) {
        val keys = if (includes.isNotEmpty()) {
            value.keys.stream().filter { s -> includes.getOrDefault(s, isIncludeAll) }.collect(Collectors.toSet())
        } else {
            value.keys.stream().filter { s -> children.containsKey(s) || isIncludeAll }.collect(Collectors.toSet())
        }
        //Process child nodes with the specified settings.
        for (childKey in keys) {
            val subQueryNode = children[childKey]
            if (subQueryNode != null) {
                subQueryNode.visit(containerPath!!, childKey, context, value, resultContainer!!)
            } else {
                val node = QueryNodeBoolean(BoolOperatorConst(true), childKey, containerPath)
                node.visit(containerPath!!, childKey, context, value, resultContainer!!)
            }
        }
    }

    override fun copy(): QueryNodeCompound {
        return QueryNodeCompound(this)
    }
}
