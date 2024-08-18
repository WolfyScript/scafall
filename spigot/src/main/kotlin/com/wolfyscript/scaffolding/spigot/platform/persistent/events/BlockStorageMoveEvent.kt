package com.wolfyscript.scaffolding.spigot.platform.persistent.events

import com.wolfyscript.scaffolding.spigot.platform.persistent.world.BlockStorage
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.*

class BlockStorageMoveEvent(override val storage: BlockStorage, private val previousBlockStorage: BlockStorage?) :
    Event(), BlockStorageEvent {
    val previousStore: Optional<BlockStorage>
        get() = Optional.ofNullable(
            previousBlockStorage
        )

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        val handlerList: HandlerList = HandlerList()
    }
}
