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

import com.wolfyscript.scaffolding.spigot.platform.compatibility.PluginIntegration
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.regex.Pattern

interface PlaceholderAPIIntegration : PluginIntegration {

    fun setPlaceholders(player: OfflinePlayer?, text: String): String

    fun setPlaceholders(player: OfflinePlayer?, text: List<String>): List<String>

    fun setPlaceholders(player: Player?, text: String): String

    fun setPlaceholders(player: Player?, text: List<String>): List<String>

    fun setBracketPlaceholders(player: OfflinePlayer?, text: String): String

    fun setBracketPlaceholders(player: OfflinePlayer?, text: List<String>): List<String>

    fun setBracketPlaceholders(player: Player?, text: String): String

    fun setBracketPlaceholders(player: Player?, text: List<String>): List<String>

    fun setRelationalPlaceholders(one: Player?, two: Player?, text: String): String

    fun setRelationalPlaceholders(one: Player?, two: Player?, text: List<String>): List<String>

    fun isRegistered(identifier: String): Boolean

    val registeredIdentifiers: Set<String>

    val placeholderPattern: Pattern

    val bracketPlaceholderPattern: Pattern

    val relationalPlaceholderPattern: Pattern

    fun containsPlaceholders(text: String?): Boolean

    fun containsBracketPlaceholders(text: String?): Boolean

    companion object {
        const val KEY: String = "PlaceholderAPI"
    }
}
