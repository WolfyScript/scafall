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
import com.wolfyscript.scafall.spigot.api.into
import com.wolfyscript.scafall.spigot.platform.world.items.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.event.player.PlayerItemMendEvent
import org.bukkit.inventory.ItemStack

class CustomDurabilityListener(plugin: Scafall) : Listener {
    private val core: Scafall = plugin

    @EventHandler
    fun onDamage(event: PlayerItemDamageEvent) {
        val customItem = ItemBuilder(event.item)
        val itemStack: ItemStack = event.item
        if (customItem.hasCustomDurability()) {
            event.isCancelled = true
            val totalDmg: Int = customItem.getCustomDamage(event.item.itemMeta) + event.damage
            if (totalDmg > customItem.getCustomDurability(event.item.itemMeta)) {
                itemStack.amount -= 1
                event.player.playSound(event.player.location, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f)
            } else {
                customItem.setCustomDamage(customItem.getCustomDamage(event.item.itemMeta) + event.damage)
            }
        }
    }

    @EventHandler
    fun onMend(event: PlayerItemMendEvent) {
        val customItem = ItemBuilder(event.item)
        if (customItem.hasCustomDurability()) {
            val repairAmount: Int = (event.experienceOrb.experience * 2).coerceAtMost(customItem.getCustomDamage(event.item.itemMeta))
            val finalTotalDmg = 0.coerceAtLeast(customItem.getCustomDamage(event.item.itemMeta) - repairAmount)
            Bukkit.getScheduler().runTask(core.corePlugin.into().plugin, Runnable { customItem.setCustomDamage(finalTotalDmg) })
        }
    }
}
