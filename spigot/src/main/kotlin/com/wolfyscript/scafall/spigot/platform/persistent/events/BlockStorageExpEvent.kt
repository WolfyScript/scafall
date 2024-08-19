package com.wolfyscript.scafall.spigot.platform.persistent.events

import com.wolfyscript.scafall.spigot.platform.persistent.world.BlockStorage
import org.bukkit.block.Block
import org.bukkit.event.HandlerList
import org.bukkit.event.block.BlockExpEvent

class BlockStorageExpEvent(block: Block, override var storage: BlockStorage, exp: Int) :
    BlockExpEvent(block, exp), BlockStorageEvent {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        val handlerList: HandlerList = HandlerList()
    }
}
