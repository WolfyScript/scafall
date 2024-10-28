package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.wrappers.world.items.data.Bees
import org.bukkit.block.Beehive
import org.bukkit.inventory.meta.BlockStateMeta

internal val beesItemMetaConverter = ItemMetaDataKeyConverter<Bees>({
    if (this is BlockStateMeta) {
        val state = blockState
        if (state is Beehive) {
            TODO("Not yet implemented")
        }
    }
    null
}, {
    if (this is BlockStateMeta) {
        val state = this.blockState
        if (state is Beehive) {
            state.entityCount
        }
    }
})

class BeesImpl : Bees {

    override val entityCount: Int = TODO("Not yet implemented")

}