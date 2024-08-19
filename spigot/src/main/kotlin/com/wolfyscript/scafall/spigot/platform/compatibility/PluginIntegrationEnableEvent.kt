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
package com.wolfyscript.scafall.spigot.platform.compatibility

import com.wolfyscript.scafall.Scafall
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * This event is called once the [PluginIntegration]s of all plugins that WolfyUtilities depends on are done.
 * That includes plugins that load data asynchronously.
 */
class PluginIntegrationEnableEvent(val core: Scafall, val integration: PluginIntegration) : Event() {

    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }

    companion object {
        private val handlers: HandlerList = HandlerList()

        val handlerList: HandlerList
            get() = handlers
    }
}
