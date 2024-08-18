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
package com.wolfyscript.scaffolding.spigot.platform.world.items.actions

import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.identifier.Key
import org.bukkit.entity.EntityType
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot

abstract class EventPlayerInteractEntityAbstract<T : PlayerInteractEntityEvent>(key: Key) : EventPlayer<DataPlayerEvent<T>>(key, DataPlayerEvent::class.java as Class<DataPlayerEvent<T>>) {
    private val hand: List<EquipmentSlot> = listOf(EquipmentSlot.HAND)
    private val entityType = listOf<EntityType>()

    override fun call(core: Scaffolding, data: DataPlayerEvent<T>) {
        val event: PlayerInteractEntityEvent = data.event
        if ((hand.isEmpty() || hand.contains(event.hand)) && (entityType.isEmpty() || entityType.contains(event.rightClicked.type))) {
            super.call(core, data)
        }
    }
}
