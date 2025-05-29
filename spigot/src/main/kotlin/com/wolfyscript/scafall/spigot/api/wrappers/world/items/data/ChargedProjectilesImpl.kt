package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.items.data.ChargedProjectiles
import org.bukkit.inventory.meta.CrossbowMeta

internal val chargedProjectilesItemMetaConverter = ItemMetaDataKeyConverter<ChargedProjectiles>(
    {
        if (this is CrossbowMeta) {
            val projectiles = chargedProjectiles.map { it.wrap() }
            return@ItemMetaDataKeyConverter ChargedProjectiles(projectiles)
        }
        null
    },
    { projectiles ->
        if (this is CrossbowMeta) {
            setChargedProjectiles(projectiles?.projectiles?.map { it.unwrap() } ?: emptyList())
        }
    }
)
