package com.wolfyscript.scaffolding.spigot.platform.persistent.events

import com.wolfyscript.scaffolding.spigot.platform.persistent.world.BlockStorage
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.*

class BlockStorageDropItemsEvent(
    var block: Block,
    val blockState: BlockState,
    override val storage: BlockStorage,
    private val player: Player?,
    val items: MutableList<Item>
) :
    Event(), BlockStorageEvent, Cancellable {
    private var cancel = false

    fun getPlayer(): Optional<Player> {
        return Optional.ofNullable(player)
    }

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
