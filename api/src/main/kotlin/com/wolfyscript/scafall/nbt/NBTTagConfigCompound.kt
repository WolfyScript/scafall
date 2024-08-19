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
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.util.stream.Collectors

@StaticNamespacedKey(key = "compound")
class NBTTagConfigCompound : NBTTagConfig {

    @JsonIgnore
    var children: MutableMap<String, NBTTagConfig> = HashMap()
        set(value) {
            field = value.entries.stream().collect(
                Collectors.toMap(
                    { it.key },
                    { entry: Map.Entry<String, NBTTagConfig> ->
                        val value = entry.value
                        value.parent = this
                        value
                    })
            )
        }

    @JsonCreator
    internal constructor() : super() {
        this.children = HashMap()
    }

    constructor(parent: NBTTagConfig?) : super(parent) {
        this.children = HashMap()
    }

    private constructor(other: NBTTagConfigCompound) : super() {
        this.children = other.children
    }

    @JsonAnySetter
    fun loadNonNestedChildren(key: String, child: NBTTagConfig) {
        //Sets the children that are specified in the root of the object without the "children" node!
        //That is supported behaviour!
        children.putIfAbsent(key, child)
        child.parent = this
    }

    @JsonSetter("children")
    private fun setJsonChildren(children: MutableMap<String, NBTTagConfig>) {
        this.children = children
    }

    @JsonGetter
    private fun getJsonChildren(): Map<String, NBTTagConfig> {
        return children
    }

    override fun copy(): NBTTagConfigCompound {
        return NBTTagConfigCompound(this)
    }
}
