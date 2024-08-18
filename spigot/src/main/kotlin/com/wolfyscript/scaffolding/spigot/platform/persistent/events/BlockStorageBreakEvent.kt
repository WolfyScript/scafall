package com.wolfyscript.scaffolding.spigot.platform.persistent.events

import com.wolfyscript.scaffolding.spigot.platform.persistent.world.BlockStorage
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class BlockStorageBreakEvent(
    /**
     * Gets the block involved in this event.
     *
     * @return The Block which block is involved in this event
     */
    var block: Block, override var storage: BlockStorage, val player: Player
) :
    Event(), BlockStorageEvent, Cancellable {
    private var cancel = false

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    override fun isCancelled(): Boolean {
        return cancel
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }

    companion object {
        val handlerList: HandlerList = HandlerList()
    }
}
