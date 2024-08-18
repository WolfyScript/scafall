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
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scaffolding.PluginWrapper

/**
 * An Operator that represents logical operators like and (&&), or (||), not (!).<br></br>
 * They evaluate at least one inner [BoolOperator], which then results in a booleanish output.
 *
 *
 *  * [LogicalOperatorAnd]
 *  * [LogicalOperatorOr]
 *  * [LogicalOperatorNot]
 *
 *
 */
abstract class LogicalOperator(
    @JacksonInject wolfyUtils: PluginWrapper, @JvmField @field:JsonProperty(
        "this"
    ) protected val thisValue: BoolOperator
) :
    BoolOperator()
