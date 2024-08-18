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

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scaffolding.eval.value_provider.ValueProvider
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "byte_array")
class NBTTagConfigByteArray : NBTTagConfigPrimitive<ByteArray> {
    @JsonCreator
    internal constructor(@JsonProperty("value") value: ValueProvider<ByteArray>) : super(value)

    constructor(parent: NBTTagConfig?, value: ValueProvider<ByteArray>) : super(parent, value)

    private constructor(other: NBTTagConfigByteArray) : super(other.value)

    override fun copy(): NBTTagConfigByteArray {
        return NBTTagConfigByteArray(this)
    }
}
