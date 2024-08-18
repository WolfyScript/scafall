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
package com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins

import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.spigot.api.into
import com.wolfyscript.scaffolding.spigot.platform.compatibility.PluginIntegrationAbstract
import com.wolfyscript.scaffolding.spigot.platform.compatibility.WUPluginIntegration
import com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.value_providers.ValueProviderFloatPAPI
import com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.value_providers.ValueProviderIntegerPAPI
import com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.value_providers.ValueProviderStringPAPI
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.events.ExpansionsLoadedEvent
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import java.util.regex.Pattern

@WUPluginIntegration(pluginName = PlaceholderAPIIntegration.KEY)
internal class PlaceholderAPIImpl(core: Scaffolding) : PluginIntegrationAbstract(core, PlaceholderAPIIntegration.KEY), PlaceholderAPIIntegration, Listener {

    override fun init(plugin: Plugin?) {
        core.logger.info("init PAPI event")
        Bukkit.getPluginManager().registerEvents(this, core.corePlugin.into().plugin)

        val valueProviderRegistry = core.registries.valueProviders
        valueProviderRegistry.register(ValueProviderFloatPAPI.KEY, ValueProviderFloatPAPI::class.java)
        valueProviderRegistry.register(ValueProviderIntegerPAPI.KEY, ValueProviderIntegerPAPI::class.java)
        valueProviderRegistry.register(ValueProviderStringPAPI.KEY, ValueProviderStringPAPI::class.java)
    }

    override fun hasAsyncLoading(): Boolean {
        return false
    }

    override fun setPlaceholders(player: OfflinePlayer?, text: String): String {
        return PlaceholderAPI.setPlaceholders(player, text)
    }

    override fun setPlaceholders(player: OfflinePlayer?, text: List<String>): List<String> {
        return PlaceholderAPI.setPlaceholders(player, text)
    }

    override fun setPlaceholders(player: Player?, text: String): String {
        return PlaceholderAPI.setPlaceholders(player, text)
    }

    override fun setPlaceholders(player: Player?, text: List<String>): List<String> {
        return PlaceholderAPI.setPlaceholders(player, text)
    }

    override fun setBracketPlaceholders(player: OfflinePlayer?, text: String): String {
        return PlaceholderAPI.setBracketPlaceholders(player, text)
    }

    override fun setBracketPlaceholders(player: OfflinePlayer?, text: List<String>): List<String> {
        return PlaceholderAPI.setBracketPlaceholders(player, text)
    }

    override fun setBracketPlaceholders(player: Player?, text: String): String {
        return PlaceholderAPI.setBracketPlaceholders(player, text)
    }

    override fun setBracketPlaceholders(player: Player?, text: List<String>): List<String> {
        return PlaceholderAPI.setBracketPlaceholders(player, text)
    }

    override fun setRelationalPlaceholders(one: Player?, two: Player?, text: String): String {
        return PlaceholderAPI.setRelationalPlaceholders(one, two, text)
    }

    override fun setRelationalPlaceholders(one: Player?, two: Player?, text: List<String>): List<String> {
        return PlaceholderAPI.setRelationalPlaceholders(one, two, text)
    }

    override fun isRegistered(identifier: String): Boolean {
        return PlaceholderAPI.isRegistered(identifier)
    }

    override val registeredIdentifiers: Set<String>
        get() = PlaceholderAPI.getRegisteredIdentifiers()

    override val placeholderPattern: Pattern
        get() = PlaceholderAPI.getPlaceholderPattern()

    override val bracketPlaceholderPattern: Pattern
        get() = PlaceholderAPI.getBracketPlaceholderPattern()

    override val relationalPlaceholderPattern: Pattern
        get() = PlaceholderAPI.getRelationalPlaceholderPattern()

    override fun containsPlaceholders(text: String?): Boolean {
        return PlaceholderAPI.containsPlaceholders(text)
    }

    override fun containsBracketPlaceholders(text: String?): Boolean {
        return PlaceholderAPI.containsBracketPlaceholders(text)
    }

    @EventHandler
    fun onEnabled(event: ExpansionsLoadedEvent?) {
        if (!this.isDoneLoading) {
            enable()
        }
    }
}
