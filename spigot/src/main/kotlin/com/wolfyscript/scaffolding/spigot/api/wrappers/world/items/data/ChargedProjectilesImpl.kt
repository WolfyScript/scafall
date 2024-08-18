package com.wolfyscript.scaffolding.spigot.api.wrappers.world.items.data

import com.wolfyscript.scaffolding.spigot.api.wrappers.world.items.ItemStackImpl
import com.wolfyscript.scaffolding.wrappers.world.items.ItemStack
import com.wolfyscript.scaffolding.wrappers.world.items.data.ChargedProjectiles
import org.bukkit.inventory.meta.CrossbowMeta

class ChargedProjectilesImpl(val projectiles: List<ItemStack>) : ChargedProjectiles {

    companion object {
        internal val ITEM_META_CONVERTER = ItemMetaDataKeyConverter<ChargedProjectiles>(
            {
                if (this is CrossbowMeta) {
                    val projectiles = chargedProjectiles.map { ItemStackImpl(it) }
                    return@ItemMetaDataKeyConverter ChargedProjectilesImpl(projectiles)
                }
                null
            },
            { TODO("Not yet implemented") }
        )

    }
}