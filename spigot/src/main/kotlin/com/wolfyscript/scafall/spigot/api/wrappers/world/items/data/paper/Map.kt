package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper

import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.wrap
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.MapId
import io.papermc.paper.datacomponent.item.MapItemColor

internal val mapIdConverter = PaperDataAPIConverter<Int>(
    {
        Result.success(unwrap().getData(DataComponentTypes.MAP_ID)?.id())
    }, {
        unwrap().setData(DataComponentTypes.MAP_ID, MapId.mapId(it))
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.MAP_ID)
        Result.success(this to true)
    }
)

internal val mapDecorationConverter = PaperDataAPIConverter(
    {
        val decorations = unwrap().getData(DataComponentTypes.MAP_DECORATIONS)
        if (decorations == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        Result.failure(NotImplementedError())
    }, {
        Result.failure(NotImplementedError())
    }, {
        unwrap().unsetData(DataComponentTypes.MAP_DECORATIONS)
        Result.success(this to true)
    }
)

internal val mapColorConverter = PaperDataAPIConverter(
    {
        val mapColor = unwrap().getData(DataComponentTypes.MAP_COLOR)
        if (mapColor == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        Result.success(mapColor.color().wrap())
    }, {
        unwrap().setData(DataComponentTypes.MAP_COLOR, MapItemColor.mapItemColor().color(it.unwrap()))
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.MAP_COLOR)
        Result.success(this to true)
    }
)
