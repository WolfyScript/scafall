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
package com.wolfyscript.scafall.spigot.platform.world.items.events

import com.wolfyscript.scafall.spigot.platform.persistent.events.BlockStoragePlaceEvent
import com.wolfyscript.scafall.spigot.platform.world.items.CustomItem
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class CustomItemPlaceEvent : Event {
    var customItem: CustomItem

    var isCancelled: Boolean
    private var canBuild: Boolean
    val blockAgainst: Block
    val blockReplacedState: BlockState
    val itemInHand: ItemStack
    var player: Player
        protected set
    val hand: EquipmentSlot
    val block: Block

    constructor(customItem: CustomItem, event: BlockPlaceEvent) {
        this.block = event.blockPlaced
        this.blockAgainst = event.blockAgainst
        this.itemInHand = event.itemInHand
        this.player = event.player
        this.blockReplacedState = event.blockReplacedState
        this.canBuild = event.canBuild()
        this.hand = event.hand
        this.isCancelled = event.isCancelled
        this.customItem = customItem
    }

    constructor(customItem: CustomItem, event: BlockStoragePlaceEvent) {
        this.block = event.blockPlaced
        this.blockAgainst = event.blockAgainst
        this.itemInHand = event.itemInHand
        this.player = event.player
        this.blockReplacedState = event.blockReplacedState
        this.canBuild = event.canBuild()
        this.hand = event.hand
        this.isCancelled = event.isCancelled
        this.customItem = customItem
    }

    constructor(
        customItem: CustomItem,
        placedBlock: Block,
        replacedBlockState: BlockState,
        placedAgainst: Block,
        itemInHand: ItemStack,
        thePlayer: Player,
        canBuild: Boolean,
        hand: EquipmentSlot
    ) {
        this.block = placedBlock
        this.blockAgainst = placedAgainst
        this.itemInHand = itemInHand
        this.player = thePlayer
        this.blockReplacedState = replacedBlockState
        this.canBuild = canBuild
        this.hand = hand
        this.isCancelled = false
        this.customItem = customItem
    }

    val blockPlaced: Block
        get() = this.block

    fun canBuild(): Boolean {
        return this.canBuild
    }

    fun setBuild(canBuild: Boolean) {
        this.canBuild = canBuild
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        val handlerList: HandlerList = HandlerList()
    }
}
