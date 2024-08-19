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

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "list/string")
class NBTTagConfigListString : NBTTagConfigListPrimitive<String, NBTTagConfigString> {
    @JsonCreator
    internal constructor(@JsonProperty("values") elements: List<NBTTagConfigString>) : super(
        elements,
        NBTTagConfigString::class.java
    )

    constructor(parent: NBTTagConfig?, elements: List<NBTTagConfigString>) : super(
        parent,
        NBTTagConfigString::class.java, elements
    )

    constructor(other: NBTTagConfigList<NBTTagConfigString>) : super(other)

    override fun copy(): NBTTagConfigListString {
        return NBTTagConfigListString(this)
    }
}
