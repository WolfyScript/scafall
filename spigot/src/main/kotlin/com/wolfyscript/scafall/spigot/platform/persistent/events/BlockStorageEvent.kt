package com.wolfyscript.scafall.spigot.platform.persistent.events

import com.wolfyscript.scafall.spigot.platform.persistent.world.BlockStorage

interface BlockStorageEvent {
    val storage: BlockStorage
}
