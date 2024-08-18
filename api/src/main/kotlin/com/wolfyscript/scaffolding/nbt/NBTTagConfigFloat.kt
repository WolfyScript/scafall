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
import com.wolfyscript.scaffolding.eval.value_provider.ValueProviderFloatConst
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import com.wolfyscript.scaffolding.nbt.NBTTagConfigFloat
import java.io.IOException

@OptionalValueSerializer(serializer = NBTTagConfigFloat.OptionalValueSerializer::class)
@StaticNamespacedKey(key = "float")
class NBTTagConfigFloat : NBTTagConfigPrimitive<Float> {
    @JsonCreator
    internal constructor(@JsonProperty("value") valueNode: ValueProvider<Float>) : super(valueNode)

    constructor(parent: NBTTagConfig?, value: ValueProvider<Float>) : super(parent, value)

    private constructor(other: NBTTagConfigFloat) : super(other)

    override fun copy(): NBTTagConfigFloat {
        return NBTTagConfigFloat(this)
    }

    class OptionalValueSerializer :
        ValueSerializer<NBTTagConfigFloat>(NBTTagConfigFloat::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: NBTTagConfigFloat,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            if (targetObject.value is ValueProviderFloatConst) {
                generator.writeObject(targetObject.value)
                return true
            }
            return false
        }
    }
}
