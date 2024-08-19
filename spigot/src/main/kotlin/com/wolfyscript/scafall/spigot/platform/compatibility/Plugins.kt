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

import com.wolfyscript.scafall.identifier.Key
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier

/**
 * Manages compatibility with other plugins. <br></br>
 * It will load plugin specific integrations that are initialised if the corresponding plugins are enabled.
 */
interface Plugins {
    /**
     * @param pluginName The name of the plugin to check for
     * @return If the plugin is loaded
     */
    fun isPluginEnabled(pluginName: String): Boolean

    fun hasWorldGuard(): Boolean

    fun hasPlotSquared(): Boolean

    fun hasLWC(): Boolean

    fun hasPlaceHolderAPI(): Boolean

    fun hasMcMMO(): Boolean

    /**
     * Checks if the integration for the specified plugin is available.
     *
     * @param pluginName The name of plugin to look for.
     * @return True if the integration for the plugin is available.
     */
    fun hasIntegration(pluginName: String): Boolean

    /**
     * Gets the integration of the specified plugin and type.<br></br>
     * In case there is no integration available, it returns null.<br></br>
     *
     * @param pluginName The plugin name to get the integration for.
     * @return The integration from the plugin; null if not available.
     */
    fun getIntegration(pluginName: String): PluginIntegration?

    /**
     * Gets the integration of the specified plugin and type.<br></br>
     * In case there is no integration available, it returns null.<br></br>
     * If it does exist it will try to cast the integration to the specified type; if that fails throws an [ClassCastException].
     *
     * @param pluginName The plugin name to get the integration for.
     * @param type The class of the plugins' integration that extends [PluginIntegration].
     * @param <T> The specified type of the [PluginIntegration]
     * @throws ClassCastException if the found [PluginIntegration] cannot be cast to [T]
     * @return The integration from the plugin of type [T]; null if not available.
    </T> */
    fun <T : PluginIntegration> getIntegration(pluginName: String, type: Class<T>): T?

    /**
     * Runs the specified callback if there is an active PluginIntegration available for that plugin.
     *
     * @param pluginName The plugin name to check for the integration.
     * @param callback The callback to run.
     */
    fun runIfAvailable(pluginName: String, callback: Consumer<PluginIntegration>)

    /**
     * Runs the specified callback if there is an active PluginIntegration available for that plugin and the if the integration is of the type specified.
     *
     * @param pluginName The plugin name to check for the integration.
     * @param type The class that extends [PluginIntegration].
     * @param callback The callback to run.
     * @param <T> The type of [PluginIntegration] to check for and use in the callback.
    </T> */
    fun <T : PluginIntegration> runIfAvailable(pluginName: String, type: Class<T>, callback: Consumer<T>)

    /**
     * Evaluates the specified callback if there is an active PluginIntegration available for that plugin and the if the integration is of the type specified.
     *
     * @param pluginName The plugin name to check for the integration.
     * @param callback The callback to run.
     * @return The value of the evaluated callback; or false if the integration doesn't exist.
     */
    fun evaluateIfAvailable(pluginName: String, callback: Function<PluginIntegration, Boolean>): Boolean

    /**
     * Evaluates the specified callback if there is an active PluginIntegration available for that plugin and the if the integration is of the type specified.
     *
     * @param pluginName The plugin name to check for the integration.
     * @param type The class that extends [PluginIntegration].
     * @param callback The callback to run.
     * @param <T> The type of [PluginIntegration] to check for and use in the callback.
     * @throws ClassCastException if the found [PluginIntegration] cannot be cast to [T]
     * @return The value of the evaluated callback; or false if the integration doesn't exist.
    </T> */
    fun <T : PluginIntegration> evaluateIfAvailable(
        pluginName: String,
        type: Class<T>,
        callback: Function<T, Boolean>
    ): Boolean

    /**
     * An unmodifiable list of all available PluginIntegrations.
     *
     * @return A list of available PluginIntegrations.
     */
    val pluginIntegrations: Collection<PluginIntegration>

    /**
     * Gets if all PluginIntegrations are loaded.
     *
     * @return True if all integrations are done loading or there is nothing to load; else false.
     */
    val isDoneLoading: Boolean

    /**
     * Registers a new PluginAdapter if the specified plugin is available.<br></br>
     * The plugin should be specified in your plugins `softdepend`.<br></br>
     * If the plugin is not available when this method is called, then it'll do nothing.
     *
     * @param pluginName The plugin name, to register the adapter for.
     * @param pluginAdapter The adapter supplier.
     */
    fun registerAdapter(pluginName: String, pluginAdapter: Supplier<PluginAdapter>)

    /**
     * Gets the registered adapter for the specified plugin and key.<br></br>
     *
     * @param pluginName The plugin name.
     * @param type The type of the adapter.
     * @param key The key of the adapter.
     * @param <T> The PluginAdapter type.
     * @return The adapter of the specified key; or null if it doesn't exist or doesn't match the type.
    </T> */
    fun <T : PluginAdapter> getAdapter(pluginName: String, type: Class<T>, key: Key): T?
}
