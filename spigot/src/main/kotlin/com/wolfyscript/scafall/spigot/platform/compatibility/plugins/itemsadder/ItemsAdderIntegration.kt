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
package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.itemsadder

import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegration
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import java.util.*

interface ItemsAdderIntegration : PluginIntegration {

    fun getStackByItemStack(itemStack: ItemStack): Optional<CustomStack>

    fun getStackInstance(namespacedID: String): Optional<CustomStack>

    fun getBlockByItemStack(itemStack: ItemStack): Optional<CustomBlock>

    fun getBlockPlaced(block: Block): Optional<CustomBlock>

    fun getBlockInstance(namespacedID: String): Optional<CustomBlock>

    companion object {
        const val KEY: String = "ItemsAdder"
    }
}
