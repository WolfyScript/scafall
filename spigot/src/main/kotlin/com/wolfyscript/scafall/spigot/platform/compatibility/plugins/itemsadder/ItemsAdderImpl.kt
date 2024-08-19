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

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.spigot.api.into
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationAbstract
import com.wolfyscript.scafall.spigot.platform.compatibility.WUPluginIntegration
import com.wolfyscript.scafall.spigot.platform.stackIdentifierParsers
import com.wolfyscript.scafall.spigot.platform.stackIdentifiers
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.util.*

@WUPluginIntegration(pluginName = ItemsAdderIntegration.KEY)
class ItemsAdderImpl protected constructor(core: Scafall) :
    PluginIntegrationAbstract(core, ItemsAdderIntegration.KEY),
    ItemsAdderIntegration, Listener {
    override fun init(plugin: Plugin?) {
        core.registries.stackIdentifierParsers.register(ItemsAdderStackIdentifierImpl.Parser())
        core.registries.stackIdentifiers.register(ItemsAdderStackIdentifierImpl::class.java)
        Bukkit.getPluginManager().registerEvents(this, core.corePlugin.into().plugin)
        Bukkit.getPluginManager().registerEvents(CustomItemListener(this), core.corePlugin.into().plugin)
    }

    override fun hasAsyncLoading(): Boolean {
        return true
    }

    @EventHandler
    fun onLoaded(event: ItemsAdderLoadDataEvent) {
        if (event.cause == ItemsAdderLoadDataEvent.Cause.FIRST_LOAD) {
            enable()
        }
    }

    override fun getStackByItemStack(itemStack: ItemStack): Optional<CustomStack> {
        return CustomStackWrapper.wrapStack(dev.lone.itemsadder.api.CustomStack.byItemStack(itemStack))
    }

    override fun getStackInstance(namespacedID: String): Optional<CustomStack> {
        return CustomStackWrapper.wrapStack(dev.lone.itemsadder.api.CustomStack.getInstance(namespacedID))
    }

    override fun getBlockByItemStack(itemStack: ItemStack): Optional<CustomBlock> {
        return CustomBlockWrapper.wrapBlock(dev.lone.itemsadder.api.CustomBlock.byItemStack(itemStack))
    }

    override fun getBlockPlaced(block: Block): Optional<CustomBlock> {
        return CustomBlockWrapper.wrapBlock(dev.lone.itemsadder.api.CustomBlock.byAlreadyPlaced(block))
    }

    override fun getBlockInstance(namespacedID: String): Optional<CustomBlock> {
        return CustomBlockWrapper.wrapBlock(dev.lone.itemsadder.api.CustomBlock.getInstance(namespacedID))
    }
}
