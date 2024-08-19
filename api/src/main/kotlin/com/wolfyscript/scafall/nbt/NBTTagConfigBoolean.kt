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
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.operator.BoolOperator
import com.wolfyscript.scafall.eval.operator.BoolOperatorConst
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.io.IOException

@OptionalValueSerializer(serializer = NBTTagConfigBoolean.OptionalValueSerializer::class)
@StaticNamespacedKey(key = "bool")
class NBTTagConfigBoolean : NBTTagConfig {
    private val value: BoolOperator

    @JsonCreator
    internal constructor(@JsonProperty("value") value: BoolOperator) : super() {
        this.value = value
    }

    constructor(parent: NBTTagConfig?, value: BoolOperator) : super(parent) {
        this.value = value
    }

    private constructor(other: NBTTagConfigBoolean) : super() {
        this.value = other.value
    }

    fun getValue(context: EvalContext): Boolean {
        return value.evaluate(context)
    }

    fun getValue(): Boolean {
        return getValue(EvalContext())
    }

    override fun copy(): NBTTagConfigBoolean {
        return NBTTagConfigBoolean(this)
    }

    class OptionalValueSerializer : ValueSerializer<NBTTagConfigBoolean>(NBTTagConfigBoolean::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: NBTTagConfigBoolean,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            if (targetObject.value is BoolOperatorConst) {
                generator.writeObject(targetObject.value)
                return true
            }
            return false
        }
    }
}
