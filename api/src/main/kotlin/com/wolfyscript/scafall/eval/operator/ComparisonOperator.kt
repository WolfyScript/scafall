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
package com.wolfyscript.scafall.eval.operator

import com.fasterxml.jackson.annotation.JacksonInject
import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProvider

/**
 * Represents comparison operators that compare values.<br></br>
 *
 *  * equal (==) [ComparisonOperatorEqual]
 *  * not equal (!=) [ComparisonOperatorNotEqual]
 *  * less (<) [ComparisonOperatorLess]
 *  * less or equal(<=) [ComparisonOperatorLessEqual]
 *  * greater (>) [ComparisonOperatorGreater]
 *  * greater or equal (>=) [ComparisonOperatorGreaterEqual]
 *
 *
 * @param <V> The type of the objects to compare. Must be the same for both objects.
</V> */
abstract class ComparisonOperator<V : Comparable<V>?> protected constructor(
    @JacksonInject wolfyUtils: PluginWrapper,
    @JvmField protected var thisValue: ValueProvider<V>,
    @JvmField protected var thatValue: ValueProvider<V>
) :
    BoolOperator() {
    abstract override fun evaluate(context: EvalContext): Boolean
}
