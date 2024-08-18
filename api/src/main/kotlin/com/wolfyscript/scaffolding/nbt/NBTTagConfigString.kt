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
import com.wolfyscript.scaffolding.eval.value_provider.ValueProviderStringConst
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import com.wolfyscript.scaffolding.nbt.NBTTagConfigString
import java.io.IOException

@OptionalValueSerializer(serializer = NBTTagConfigString.OptionalValueSerializer::class)
@StaticNamespacedKey(key = "string")
class NBTTagConfigString : NBTTagConfigPrimitive<String> {
    @JsonCreator
    internal constructor(@JsonProperty("value") value: ValueProvider<String>) : super(value)

    constructor(parent: NBTTagConfig?, value: ValueProvider<String>) : super(parent, value)

    constructor(other: NBTTagConfigPrimitive<String>) : super(other)

    override fun copy(): NBTTagConfigString {
        return NBTTagConfigString(this)
    }

    class OptionalValueSerializer :
        ValueSerializer<NBTTagConfigString>(NBTTagConfigString::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: NBTTagConfigString,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            if (targetObject.value is ValueProviderStringConst) {
                generator.writeObject(targetObject.value)
                return true
            }
            return false
        }
    }
}
