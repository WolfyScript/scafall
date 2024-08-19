package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.wrappers.world.items.data.AttributeModifiers

class AttributeModifiersImpl(override val showInTooltip: Boolean) : AttributeModifiers {

    companion object {
        internal val ITEM_META_CONVERTER = ItemMetaDataKeyConverter<AttributeModifiers>(
            { TODO("Not yet implemented") },
            { TODO("Not yet implemented") }
        )
    }

}