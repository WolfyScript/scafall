package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.wrappers.world.items.data.DyedColor

class DyedColorImpl : DyedColor{

    companion object {
        internal val ITEM_META_CONVERTER = ItemMetaDataKeyConverter<DyedColor>(
            { TODO("Not yet implemented") },
            { TODO("Not yet implemented") }
        )
    }

    override fun rgb(): Int {
        TODO("Not yet implemented")
    }

    override val showInTooltip: Boolean
        get() = TODO("Not yet implemented")
}