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
package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.mmoitems

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationAbstract
import com.wolfyscript.scafall.spigot.platform.compatibility.WUPluginIntegration
import com.wolfyscript.scafall.spigot.platform.stackIdentifierParsers
import com.wolfyscript.scafall.spigot.platform.stackIdentifiers
import org.bukkit.plugin.Plugin

@WUPluginIntegration(pluginName = MMOItemsImpl.PLUGIN_NAME)
class MMOItemsImpl protected constructor(core: Scafall) :
    PluginIntegrationAbstract(core, PLUGIN_NAME) {
    override fun init(plugin: Plugin?) {
        core.registries.stackIdentifierParsers.register(MMOItemsStackIdentifierImpl.Parser())
        core.registries.stackIdentifiers.register(MMOItemsStackIdentifierImpl::class.java)
    }

    override fun hasAsyncLoading(): Boolean {
        return false
    }

    companion object {
        const val PLUGIN_NAME: String = "MMOItems"
    }
}
