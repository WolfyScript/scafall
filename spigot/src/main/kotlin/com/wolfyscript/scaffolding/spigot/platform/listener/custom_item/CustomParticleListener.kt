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
package com.wolfyscript.scaffolding.spigot.platform.listener.custom_item

import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.spigot.api.persistentStorage
import com.wolfyscript.scaffolding.spigot.platform.persistent.world.player.PlayerParticleEffectData
import com.wolfyscript.scaffolding.spigot.platform.persistent.world.player.PlayerStorage
import com.wolfyscript.scaffolding.spigot.platform.world.items.CustomItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class CustomParticleListener(private val core: Scaffolding) : Listener {

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        val playerStorage: PlayerStorage = core.persistentStorage.getOrCreatePlayerStorage(event.player)
        playerStorage.computeIfAbsent(
            PlayerParticleEffectData::class.java
        ) { PlayerParticleEffectData() }
    }

    @EventHandler
    fun onItemHeld(event: PlayerItemHeldEvent) {
        val player: Player = event.player
        val playerInventory = player.inventory
        val newItem: ItemStack? = playerInventory.getItem(event.newSlot)
        val item = CustomItem.getByItemStack(newItem)
        getPlayerParticleData(player)?.stopActiveParticleEffect(EquipmentSlot.HAND)
        item?.particleContent?.spawn(player, EquipmentSlot.HAND)
    }

    @EventHandler
    fun onSwitch(event: PlayerSwapHandItemsEvent) {
        val player: Player = event.player
        val data = getPlayerParticleData(player)
        data?.stopActiveParticleEffect(EquipmentSlot.HAND)
        data?.stopActiveParticleEffect(EquipmentSlot.OFF_HAND)
        val mainHand = CustomItem.getByItemStack(event.mainHandItem)
        mainHand?.particleContent?.spawn(player, EquipmentSlot.HAND)
        val offHand = CustomItem.getByItemStack(event.offHandItem)
        offHand?.particleContent?.spawn(player, EquipmentSlot.OFF_HAND)
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        val player: Player = event.player
        val currentItem = player.inventory.itemInMainHand

        if (currentItem.type == Material.AIR || currentItem.amount <= 0) {
            getPlayerParticleData(player)?.stopActiveParticleEffect(EquipmentSlot.HAND)
        }
    }

    @EventHandler
    fun onPickup(event: EntityPickupItemEvent) {
    }

    private fun getPlayerParticleData(player: Player) : PlayerParticleEffectData? {
        return core.persistentStorage.getOrCreatePlayerStorage(player).getData(PlayerParticleEffectData::class.java)
    }
}
