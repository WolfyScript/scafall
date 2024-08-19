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

import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.JsonNode
import com.wolfyscript.scafall.config.jackson.JacksonUtil
import com.wolfyscript.scafall.eval.context.EvalContext
import de.tr7zw.changeme.nbtapi.NBTCompound
import de.tr7zw.changeme.nbtapi.NBTContainer
import de.tr7zw.changeme.nbtapi.NBTType
import java.io.File
import java.io.IOException
import java.util.*
import java.util.stream.Collectors

// Override the QueryNode settings, as this class is not registered!
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, defaultImpl = NBTQuery::class)
class NBTQuery : QueryNodeCompound {
    @JsonCreator
    constructor() : super("", "") {
        this.nbtType = NBTType.NBTTagCompound
        this.includes = HashMap()
        this.required = HashMap()
        this.children = HashMap()
    }

    private constructor(other: NBTQuery) : super(other) {
        this.children = other.children.entries.stream().collect(
            Collectors.toMap(
                { it.key },
                { entry: Map.Entry<String?, QueryNode<*>?> -> entry.value!!.copy() })
        )
    }

    @JsonAnySetter
    override fun loadNonNestedChildren(key: String, node: JsonNode?) {
        // Sets the children that are specified in the root of the object without the "children" node!
        // That is supported behaviour!
        loadFrom(node, "", key).ifPresent { queryNode: QueryNode<*>? ->
            children.putIfAbsent(
                key,
                queryNode!!
            )
        }
    }

    fun run(input: NBTCompound?): NBTCompound {
        return run(input, EvalContext())
    }

    fun run(input: NBTCompound?, context: EvalContext): NBTCompound {
        val container = NBTContainer()
        // Start at the root of the container
        applyChildrenToCompound("", context, input!!, container)
        return container
    }

    override fun copy(): NBTQuery {
        return NBTQuery(this)
    }


    companion object {
        fun of(file: File?): Optional<NBTQuery> {
            try {
                return Optional.ofNullable(JacksonUtil.objectMapper.readValue(file, NBTQuery::class.java))
            } catch (e: IOException) {
                e.printStackTrace()
                return Optional.empty()
            }
        }
    }
}
