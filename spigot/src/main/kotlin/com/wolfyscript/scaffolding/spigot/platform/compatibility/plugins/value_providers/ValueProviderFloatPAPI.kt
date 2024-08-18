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
package com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.value_providers

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scaffolding.eval.context.EvalContext
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.scaffoldingKey

class ValueProviderFloatPAPI @JsonCreator protected constructor(@JsonProperty("value") value: String) : ValueProviderPlaceholderAPI<Float>(KEY, value) {

    override fun getValue(context: EvalContext): Float {
        val result = getPlaceholderValue(context)
        if (result.isBlank()) return Float.NaN
        return try {
            result.toFloat()
        } catch (ex: NumberFormatException) {
            0f
        }
    }

    companion object {
        val KEY: Key = scaffoldingKey("float/papi")
    }
}
