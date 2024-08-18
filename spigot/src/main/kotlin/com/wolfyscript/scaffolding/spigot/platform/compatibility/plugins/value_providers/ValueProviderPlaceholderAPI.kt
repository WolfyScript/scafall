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

import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scaffolding.ScaffoldingProvider.Companion.get
import com.wolfyscript.scaffolding.eval.context.EvalContext
import com.wolfyscript.scaffolding.eval.value_provider.AbstractValueProvider
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.spigot.api.compatibilityManager
import com.wolfyscript.scaffolding.spigot.api.wrappers.unwrap
import com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.PlaceholderAPIIntegration
import com.wolfyscript.scaffolding.wrappers.world.entity.Player
import org.bukkit.OfflinePlayer

abstract class ValueProviderPlaceholderAPI<V> protected constructor(
    key: Key, @field:JsonProperty("value") val placeholder: String
) : AbstractValueProvider<V>(key) {
    protected fun getPlaceholderValue(context: EvalContext): String {
        val player = (context.getVariable("player") as Player?)?.unwrap()
        val integration = get().compatibilityManager.plugins.getIntegration(PlaceholderAPIIntegration.KEY, PlaceholderAPIIntegration::class.java)
        if (player != null && integration != null) {
            val result: String = integration.setPlaceholders(player, placeholder)
            return integration.setBracketPlaceholders(player, result)
        }
        return ""
    }
}
