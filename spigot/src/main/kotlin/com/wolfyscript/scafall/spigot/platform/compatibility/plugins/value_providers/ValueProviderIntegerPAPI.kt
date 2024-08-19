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
package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.value_providers

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.defaultKey

class ValueProviderIntegerPAPI @JsonCreator protected constructor(@JsonProperty("value") value: String) : ValueProviderPlaceholderAPI<Int>(KEY, value) {

    override fun getValue(context: EvalContext): Int {
        val result = getPlaceholderValue(context)
        if (result.isBlank()) return 0
        return try {
            result.toInt()
        } catch (ex: NumberFormatException) {
            0
        }
    }

    companion object {
        val KEY: Key = defaultKey("int/papi")
    }
}
