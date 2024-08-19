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

import com.wolfyscript.scafall.spigot.platform.persistent.events.BlockStorageBreakEvent
import com.wolfyscript.scafall.spigot.platform.world.items.CustomItem
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CustomItemBreakEvent : Event {
    var exp: Int
    var block: Block
        protected set
    val player: Player
    var isDropItems: Boolean
    var isCancelled: Boolean = false
    var customItem: CustomItem

    constructor(customItem: CustomItem, event: BlockStorageBreakEvent) {
        this.player = event.player
        this.isDropItems = true
        this.exp = 0
        this.block = event.block
        this.customItem = customItem
    }

    constructor(exp: Int, block: Block, player: Player, dropItems: Boolean, cancel: Boolean, customItem: CustomItem) {
        this.exp = exp
        this.block = block
        this.player = player
        this.isDropItems = dropItems
        this.isCancelled = cancel
        this.customItem = customItem
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        val handlerList: HandlerList = HandlerList()
    }
}
