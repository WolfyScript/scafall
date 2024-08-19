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
package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.eval.context.EvalContext
import java.io.IOException

@OptionalValueSerializer(serializer = ValueProviderDoubleConst.ValueSerializer::class)
@StaticNamespacedKey(key = "double/const")
class ValueProviderDoubleConst @JsonCreator constructor(
    @param:JsonProperty("value") override val value: Double
) : AbstractValueProvider<Double>(), ValueProviderDouble {
    override fun getValue(context: EvalContext): Double {
        return value
    }

    class ValueSerializer : com.wolfyscript.scafall.config.jackson.ValueSerializer<ValueProviderDoubleConst>(
        ValueProviderDoubleConst::class.java
    ) {
        @Throws(IOException::class)
        override fun serialize(
            valueProvider: ValueProviderDoubleConst,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            generator.writeString("${valueProvider.value}d")
            return true
        }
    }
}
