package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.wrappers.world.items.data.DyedColor
import org.bukkit.Color
import org.bukkit.inventory.meta.LeatherArmorMeta

internal val dyedColorItemMetaConverter = ItemMetaDataKeyConverter<DyedColor>(
    {
        if (this is LeatherArmorMeta) {
            DyedColor(false, this.color.asRGB())
        }

        null
    },
    {
        if (this is LeatherArmorMeta) {
            setColor(Color.fromBGR(it.rgb))
        }
    }
)
