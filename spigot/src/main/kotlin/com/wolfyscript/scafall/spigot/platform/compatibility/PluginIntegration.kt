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
import com.wolfyscript.scafall.identifier.Key

interface PluginIntegration {
    /**
     * Checks if the integrated plugin loads data async.
     *
     * @return True if the integration has an async loader; false otherwise.
     */
    fun hasAsyncLoading(): Boolean

    /**
     * Gets the plugin name of this integration.
     *
     * @return The name of the plugin associated with this integration.
     */
    val associatedPlugin: String

    /**
     * Gets the plugin core.
     *
     * @return The plugin core.
     */
    val core: Scafall

    /**
     * Checks if the integration is done loading.<br></br>
     * For sync integrations this is usually always true.<br></br>
     *
     * @return True if the integration is done loading; else false.
     */
    val isDoneLoading: Boolean

    /**
     * Gets if this Integration should be ignored, as its plugin failed
     * to load or had errors.
     *
     * @return True if this integration should be ignored.
     */
    fun shouldBeIgnored(): Boolean

    /**
     * Gets the registered adapter of this plugin and specified key.<br></br>
     *
     * @param type The type of the adapter.
     * @param key The key of the adapter.
     * @param <T> The PluginAdapter type.
     * @return The adapter of the specified key; or null if it doesn't exist or doesn't match the type.
    </T> */
    fun <T : PluginAdapter> getAdapter(type: Class<T>, key: Key): T?
}
