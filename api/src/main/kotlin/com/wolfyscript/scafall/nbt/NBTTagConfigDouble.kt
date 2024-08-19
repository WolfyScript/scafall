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
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.config.jackson.ValueSerializer
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.eval.value_provider.ValueProviderDoubleConst
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.io.IOException

@OptionalValueSerializer(serializer = NBTTagConfigDouble.OptionalValueSerializer::class)
@StaticNamespacedKey(key = "double")
class NBTTagConfigDouble : NBTTagConfigPrimitive<Double> {
    @JsonCreator
    internal constructor(@JsonProperty("value") value: ValueProvider<Double>) : super(value)

    constructor(parent: NBTTagConfig?, value: ValueProvider<Double>) : super(parent, value)

    private constructor(other: NBTTagConfigDouble) : super(other)

    override fun copy(): NBTTagConfigDouble {
        return NBTTagConfigDouble(this)
    }

    class OptionalValueSerializer : ValueSerializer<NBTTagConfigDouble>(NBTTagConfigDouble::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: NBTTagConfigDouble,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            if (targetObject.value is ValueProviderDoubleConst) {
                generator.writeObject(targetObject.value)
                return true
            }
            return false
        }
    }
}
