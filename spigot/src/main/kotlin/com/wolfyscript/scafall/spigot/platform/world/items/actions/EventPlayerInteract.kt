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
package com.wolfyscript.scafall.spigot.platform.world.items.actions

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.scaffoldingKey
import org.bukkit.block.BlockFace
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class EventPlayerInteract protected constructor() : EventPlayer<DataPlayerEvent<PlayerInteractEvent>>(
    KEY,
    DataPlayerEvent::class.java as Class<DataPlayerEvent<PlayerInteractEvent>>
) {
    private var eventAction: List<Action> = listOf(Action.RIGHT_CLICK_AIR)
    private var hand: List<EquipmentSlot?> = listOf(EquipmentSlot.HAND)
    private var blockFace = listOf<BlockFace>()

    override fun call(core: Scafall, data: DataPlayerEvent<PlayerInteractEvent>) {
        val event = data.event
        if ((eventAction.isEmpty() || eventAction.contains(event.action)) && (hand.isEmpty() || hand.contains(
                event.hand
            )) && (blockFace.isEmpty() || blockFace.contains(event.blockFace))
        ) {
            super.call(core, data)
        }
    }

    fun setEventAction(eventAction: List<Action>) {
        this.eventAction = eventAction
    }

    fun setHand(hand: List<EquipmentSlot?>) {
        this.hand = hand
    }

    fun setBlockFace(blockFace: List<BlockFace>) {
        this.blockFace = blockFace
    }

    companion object {
        val KEY: Key = scaffoldingKey("player/interact")
    }
}
