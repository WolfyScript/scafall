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
package com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.eco

import com.wolfyscript.scaffolding.spigot.platform.compatibility.PluginIntegration
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

interface EcoIntegration : PluginIntegration {
    /**
     * Checks if the ItemStack is a CustomItem from eco.
     *
     * @param itemStack The bukkit stack to check.
     * @return True if the ItemStack is an eco CustomItem; otherwise false.
     */
    fun isCustomItem(itemStack: ItemStack?): Boolean

    /**
     * Gets the NamespacedKey of the CustomItem, that belongs to the ItemStack.
     *
     *
     * @param itemStack The ItemStack to get the CustomItem from.
     * @return The NamespacedKey of the CustomItem; null if the ItemStack is not a CustomItem
     */
    fun getCustomItem(itemStack: ItemStack?): NamespacedKey?

    /**
     * Gets the CustomItem of the specified key.
     *
     * @param key The key of the CustomItem.
     * @return The ItemStack of the CustomItem.
     */
    fun lookupItem(key: String): ItemStack

    /**
     * Gets the CustomItem of the specified key.
     *
     * @param key The key of the CustomItem.
     * @return The ItemStack of the CustomItem.
     */
    fun lookupItem(key: NamespacedKey): ItemStack {
        return lookupItem(key.toString())
    }

    companion object {
        /**
         * The name of the plugin, that this integration belongs to.
         */
        const val KEY: String = "eco"
    }
}
