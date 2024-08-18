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
package com.wolfyscript.scaffolding.eval.operator

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.wolfyscript.scaffolding.PluginWrapper
import com.wolfyscript.scaffolding.config.jackson.KeyedBaseType
import com.wolfyscript.scaffolding.config.jackson.OptionalValueDeserializer
import com.wolfyscript.scaffolding.config.jackson.ValueDeserializer
import com.wolfyscript.scaffolding.eval.context.EvalContext
import com.wolfyscript.scaffolding.identifier.Key
import java.io.IOException

/**
 * An Operator that evaluates into a booleanish value.
 */
@KeyedBaseType(baseType = Operator::class)
@OptionalValueDeserializer(deserializer = BoolOperator.OptionalValueDeserializer::class)
abstract class BoolOperator : Operator {
    constructor(namespacedKey: Key) : super(namespacedKey)

    constructor(@JacksonInject wolfyUtils: PluginWrapper) : super(wolfyUtils)

    abstract fun evaluate(context: EvalContext): Boolean

    class OptionalValueDeserializer : ValueDeserializer<BoolOperator>(BoolOperator::class.java) {

        @Throws(IOException::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): BoolOperator {
            val node = p.readValueAsTree<JsonNode>()
            return BoolOperatorConst(
                ctxt.findInjectableValue(
                    PluginWrapper::class.java.name,
                    null,
                    null
                ) as PluginWrapper, node.asBoolean()
            )
        }

    }
}