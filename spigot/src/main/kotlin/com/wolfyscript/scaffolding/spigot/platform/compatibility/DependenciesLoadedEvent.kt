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
package com.wolfyscript.scaffolding.spigot.platform.compatibility

import com.wolfyscript.scaffolding.Scaffolding
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * This event is called once the [PluginIntegration]s of all plugins that WolfyUtilities depends on are done.
 * That includes plugins that load data asynchronously.
 */
class DependenciesLoadedEvent(
    /**
     * Gets the core [Scaffolding]
     * @return The core of the plugin.
     */
    val core: Scaffolding
) : Event() {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        val handlerList: HandlerList = HandlerList()
    }
}
