package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.Repairable
import com.wolfyscript.scafall.wrappers.world.items.data.Tool

internal val toolItemMetaConverter = ItemMetaDataKeyConverter<Tool>(
    {
        if (hasTool()) {
            val tool = tool


        }
        null
    },
    {

    }
)

internal val repairableItemMetaConverter = ItemMetaDataKeyConverter<Repairable>(
    {
        null
    },
    {

    }
)

internal val tooltipStyleItemMetaConverter = ItemMetaDataKeyConverter<Key>(
    {
        if (hasTooltipStyle()) {
            return@ItemMetaDataKeyConverter tooltipStyle?.toAPI()
        }
        null
    },
    {
        tooltipStyle = it.bukkit()
    }
)