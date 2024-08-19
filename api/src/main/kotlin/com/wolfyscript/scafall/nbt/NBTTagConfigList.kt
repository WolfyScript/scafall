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
package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.*
import java.util.*
import kotlin.collections.ArrayList

abstract class NBTTagConfigList<VAL : NBTTagConfig> : NBTTagConfig {
    @JsonIgnore
    val elementType: Class<VAL>

    @JsonIgnore
    val elements: List<Element<VAL>>
    var values: List<VAL> = ArrayList()
        set(value) {
            field = value.stream().peek { it.parent = this }.toList()
        }

    @JsonCreator
    internal constructor(@JsonProperty("values") values: List<VAL>, elementClass: Class<VAL>) : super() {
        this.elementType = elementClass
        this.values = values
        this.elements = ArrayList()
    }

    constructor(parent: NBTTagConfig?, elementType: Class<VAL>, values: List<VAL>) : super(parent) {
        this.elementType = elementType
        this.values = values
        this.elements = ArrayList()
    }

    protected constructor(other: NBTTagConfigList<VAL>) : super() {
        this.elementType = other.elementType
        this.values = other.values.map { it.copy() as VAL }
        this.elements = other.elements.stream().map { element: Element<VAL> ->
            val copyElem = element.copy()
            copyElem.value.parent = this
            copyElem
        }.toList()
    }

    class Element<VAL : NBTTagConfig> private constructor(other: Element<VAL>) {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private var index: Int?

        @get:JsonGetter
        @set:JsonSetter
        @JsonInclude(JsonInclude.Include.NON_NULL)
        var value: VAL

        init {
            this.index = other.index
            this.value = other.value.copy() as VAL
        }

        private fun index(): Optional<Int> {
            return Optional.ofNullable(index)
        }

        private fun value(): Optional<VAL> {
            return Optional.ofNullable(value)
        }

        @JsonSetter
        fun setIndex(index: Int) {
            this.index = index
        }

        @JsonGetter
        private fun getIndex(): Int? {
            return index
        }

        fun copy(): Element<VAL> {
            return Element(this)
        }
    }
}
