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
package com.wolfyscript.scafall.spigot.platform.listener.custom_item

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.platform.customItems
import com.wolfyscript.scafall.spigot.platform.registry.RegistryCustomItem
import com.wolfyscript.scafall.spigot.platform.world.items.CustomItem
import com.wolfyscript.scafall.spigot.platform.world.items.actions.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.*

class CustomItemPlayerListener(core: Scafall) : Listener {
    private val customItems: RegistryCustomItem = core.registries.customItems

    private fun <T : PlayerEvent?> callEvent(item: CustomItem, eventKey: Key, bukkitEvent: T) {
        item.actionSettings.callEvent(eventKey, DataPlayerEvent(bukkitEvent, bukkitEvent!!.player, item))
    }

    @EventHandler
    private fun onClick(event: PlayerInteractEvent) {
        val item = customItems.getByItemStack(event.item)
        item?.run {
            callEvent(this, EventPlayerInteract.KEY, event)
        }
    }

    @EventHandler
    private fun onConsume(event: PlayerItemConsumeEvent) {
        val item = customItems.getByItemStack(event.item)
        item?.run {
            callEvent(this, EventPlayerConsumeItem.KEY, event)
        }
    }

    @EventHandler
    private fun onInteractEntity(event: PlayerInteractEntityEvent) {
        val item = customItems.getByItemStack(event.player.equipment.getItem(event.hand))
        item?.let { customItem ->
            callEvent(customItem, EventPlayerInteractEntity.KEY, event)
        }
    }

    @EventHandler
    private fun onInteractAtEntity(event: PlayerInteractAtEntityEvent) {
        val item = customItems.getByItemStack(event.player.equipment.getItem(event.hand))
        item?.let { customItem ->
            callEvent(customItem, EventPlayerInteractAtEntity.KEY, event)
        }
    }

    @EventHandler
    private fun onItemBreak(event: PlayerItemBreakEvent) {
        val item = customItems.getByItemStack(event.brokenItem)
        item?.let { customItem ->
            callEvent(customItem, EventPlayerItemBreak.KEY, event)
        }
    }

    @EventHandler
    private fun onItemDamage(event: PlayerItemDamageEvent) {
        val item = customItems.getByItemStack(event.item)
        item?.let { customItem ->
            callEvent(customItem, EventPlayerItemDamage.KEY, event)
        }
    }

    @EventHandler
    private fun onItemDrop(event: PlayerDropItemEvent) {
        val item = customItems.getByItemStack(event.itemDrop.itemStack)
        item?.let { customItem ->
            callEvent(customItem, EventPlayerItemDrop.KEY, event)
        }
    }

    @EventHandler
    private fun onItemHandSwap(event: PlayerSwapHandItemsEvent) {
        var item = customItems.getByItemStack(event.mainHandItem)
        if (item != null) {
            callEvent(item.get(), EventPlayerItemHandSwap.KEY, event)
            return
        }
        item = customItems.getByItemStack(event.offHandItem)
        item?.run {
            callEvent(this, EventPlayerItemHandSwap.KEY, event)
        }
    }

    @EventHandler
    private fun onItemHeld(event: PlayerItemHeldEvent) {
        //TODO: Handle the item from the previous and new slot
    }
}
