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
import com.wolfyscript.scafall.eval.context.EvalContext
import de.tr7zw.nbtapi.NBTCompound
import de.tr7zw.nbtapi.NBTList
import de.tr7zw.nbtapi.NBTType
import java.util.*

abstract class QueryNodeList<VAL> : QueryNode<NBTList<VAL>> {
    @JsonIgnore
    private val elementType: Class<VAL>
    val elements: List<Element<VAL>>

    @JsonCreator
    constructor(
        @JsonProperty("elements") elements: List<Element<VAL>>,
        @JacksonInject("key") key: String,
        @JacksonInject("parent_path") path: String?,
        elementType: NBTType,
        elementClass: Class<VAL>
    ) : super(key, path) {
        this.elementType = elementClass
        this.nbtType = elementType
        this.elements = elements
    }

    protected constructor(other: QueryNodeList<VAL>) : super(other.key, other.parentPath) {
        this.nbtType = other.nbtType
        this.elementType = other.elementType
        this.elements = other.elements.stream().map { obj: Element<VAL> -> obj.copy() }.toList()
    }

    override fun check(key: String?, nbtType: NBTType, context: EvalContext, value: NBTList<VAL>): Boolean {
        return !value.isEmpty
    }

    override fun readValue(path: String?, key: String?, parent: NBTCompound): NBTList<VAL>? {
        return readList(key, parent)
    }

    override fun applyValue(
        path: String,
        key: String,
        context: EvalContext,
        value: NBTList<VAL>,
        resultContainer: NBTCompound
    ) {
        val newPath = "$path.$key"
        val list: NBTList<VAL>? = readList(key, resultContainer)
        if (list != null && !value.isEmpty) {
            context.setVariable(newPath + "_size", list.size)
            for (element in elements) {
                element.index().ifPresentOrElse(
                    { index: Int ->
                        var index = index
                        if (index < 0) {
                            index =
                                value.size + (index % value.size) //Convert the negative index to a positive reverted index, that starts from the end.
                        }
                        index %= value.size //Prevent out of bounds
                        if (value.size > index) {
                            val fIndex = index
                            element.value()?.visit(newPath, fIndex, context, value, list) ?: run { list.add(value[fIndex]) }
                        }
                    },
                    {
                        element.value()?.let { valQueryNode ->
                            for (i in value.indices) {
                                context.setVariable(newPath + "_index", i)
                                valQueryNode.visit(newPath, i, context, value, list)
                            }
                        }
                    })
            }
        }
    }

    protected fun readList(key: String?, container: NBTCompound): NBTList<VAL>? {
        when (elementType) {
            Int::class.java -> {
                return container.getIntegerList(key) as NBTList<VAL>
            }

            Long::class.java -> {
                return container.getLongList(key) as NBTList<VAL>
            }

            Double::class.java -> {
                return container.getDoubleList(key) as NBTList<VAL>
            }

            Float::class.java -> {
                return container.getFloatList(key) as NBTList<VAL>
            }

            String::class.java -> {
                return container.getStringList(key) as NBTList<VAL>
            }

            NBTCompound::class.java -> {
                return container.getCompoundList(key) as NBTList<VAL>
            }

            IntArray::class.java -> {
                return container.getIntArrayList(key) as NBTList<VAL>
            }

            else -> return null
        }
    }

    class Element<VAL> {
        private var index: Int?

        @get:JsonGetter
        @set:JsonSetter
        var value: QueryNode<VAL>?

        constructor() {
            this.index = null
            this.value = null
        }

        private constructor(other: Element<VAL>) {
            this.index = other.index
            this.value = other.value!!.copy()
        }

        fun index(): Optional<Int> {
            return Optional.ofNullable(index)
        }

        fun value(): QueryNode<VAL>? {
            return value
        }

        @JsonSetter
        fun setIndex(index: Int) {
            this.index = index
        }

        @JsonGetter
        private fun getIndex(): Int {
            return index!!
        }

        fun copy(): Element<VAL> {
            return Element(this)
        }
    }
}
