package com.wolfyscript.scaffolding.spigot.platform.persistent.events

import com.wolfyscript.scaffolding.spigot.platform.persistent.world.BlockStorage

interface BlockStorageEvent {
    val storage: BlockStorage
}
