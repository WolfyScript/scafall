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
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scaffolding.config.jackson.OptionalValueSerializer
import com.wolfyscript.scaffolding.config.jackson.ValueSerializer
import com.wolfyscript.scaffolding.eval.value_provider.ValueProvider
import com.wolfyscript.scaffolding.eval.value_provider.ValueProviderIntegerConst
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import com.wolfyscript.scaffolding.nbt.NBTTagConfigInt
import java.io.IOException

@OptionalValueSerializer(serializer = NBTTagConfigInt.OptionalValueSerializer::class)
@StaticNamespacedKey(key = "int")
class NBTTagConfigInt : NBTTagConfigPrimitive<Int> {
    @JsonCreator
    internal constructor(@JsonProperty("value") value: ValueProvider<Int>) : super(value)

    constructor(parent: NBTTagConfig?, value: ValueProvider<Int>) : super(parent, value)

    constructor(other: NBTTagConfigPrimitive<Int>) : super(other)

    override fun copy(): NBTTagConfigPrimitive<Int> {
        return NBTTagConfigInt(this)
    }

    class OptionalValueSerializer :
        ValueSerializer<NBTTagConfigInt>(NBTTagConfigInt::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: NBTTagConfigInt,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            if (targetObject.value is ValueProviderIntegerConst) {
                generator.writeObject(targetObject.value)
                return true
            }
            return false
        }
    }
}
