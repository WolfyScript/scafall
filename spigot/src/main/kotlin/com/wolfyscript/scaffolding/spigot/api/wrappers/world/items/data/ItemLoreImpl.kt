package com.wolfyscript.scaffolding.spigot.api.wrappers.world.items.data

import com.wolfyscript.scaffolding.wrappers.world.items.data.ItemLore
import net.kyori.adventure.text.Component

class ItemLoreImpl : ItemLore {

    companion object {
        internal val ITEM_META_CONVERTER = ItemMetaDataKeyConverter<ItemLore>(
            { TODO("Not yet implemented") },
            { TODO("Not yet implemented") }
        )
    }

    override fun lines(): MutableList<Component> {
        TODO("Not yet implemented")
    }

}