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
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.spigot.api.compatibilityManager
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

/**
 * To add a PluginIntegration you need to extend this class and add the annotation [WUPluginIntegration] to that class.<br></br>
 * <br></br>
 * The constructor must have only one parameter of type [WolfyCoreCommon], that is passed to the super class.<br></br>
 * <br></br>
 * To effectively pass the plugin name to the annotation and PluginIntegration it is recommended to create a constant.
 * <br></br>
 * For example:
 * <pre>
 * `
 *
 * @WUPluginInteration(pluginName = MyIntegration.PLUGIN_NAME)
 * public class MyIntegration extends PluginIntegration {
 *
 * static final String PLUGIN_NAME = "YOUR_PLUGIN_NAME";
 *
 * protected MyIntegration(WolfyCoreBukkit core) {
 * super(core, PLUGIN_NAME);
 * }
 *
 * ...
 *
 * }
` *
</pre> *
 */
abstract class PluginIntegrationAbstract
/**
 * The main constructor that is called whenever the integration is created.<br></br>
 *
 * @param core The WolfyUtilCore.
 * @param pluginName The name of the associated plugin.
 */ protected constructor(override val core: Scaffolding, private val pluginName: String) :
    PluginIntegration {
    private var enabled = false
    private var ignore = false

    /**
     * This method is called if the plugin is enabled.
     * @param plugin The plugin, that was enabled.
     */
    abstract fun init(plugin: Plugin?)

    /**
     * Checks if the integrated plugin loads data async.
     *
     * @return True if the integration has an async loader; false otherwise.
     */
    abstract override fun hasAsyncLoading(): Boolean

    override val associatedPlugin: String
        get() = pluginName

    override val isDoneLoading: Boolean
        get() = enabled

    /**
     * Marks this integration as done. That tells the system that, the integrations' plugin is done with loading all its data.<br></br>
     * For example, usually plugins with async data loading will provide a listener that will be called once done. <br></br>
     * This method can then be used inside that event to mark it as done.
     */
    fun enable() {
        if (!this.enabled) {
            this.enabled = true
            core.logger.info("Enabled plugin integration for $associatedPlugin")
            Bukkit.getPluginManager().callEvent(PluginIntegrationEnableEvent(core, this))
            (core.compatibilityManager.plugins as PluginsBukkit).checkDependencies()
        }
    }

    fun disable() {
        this.enabled = false
    }

    override fun shouldBeIgnored(): Boolean {
        return ignore
    }

    fun ignore() {
        disable()
        this.ignore = true
    }

    override fun <T : PluginAdapter> getAdapter(type: Class<T>, key: Key): T? {
        return core.compatibilityManager.plugins.getAdapter(pluginName, type, key)
    }
}
