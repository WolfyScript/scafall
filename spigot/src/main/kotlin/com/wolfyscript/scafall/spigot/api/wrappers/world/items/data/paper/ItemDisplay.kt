package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.platform.world.items.actions.Data
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.ItemLore
import io.papermc.paper.datacomponent.DataComponentTypes

internal val itemNameConverter = PaperDataAPIConverter(
    {
        Result.success(unwrap().getData(DataComponentTypes.ITEM_NAME))
    }, {
        unwrap().setData(DataComponentTypes.ITEM_NAME, it)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.ITEM_NAME)
        Result.success(this to true)
    }
)

internal val customNameConverter = PaperDataAPIConverter(
    {
        Result.success(unwrap().getData(DataComponentTypes.CUSTOM_NAME))
    }, {
        unwrap().setData(DataComponentTypes.CUSTOM_NAME, it)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.CUSTOM_NAME)
        Result.success(this to true)
    }
)

internal val itemModelConverter = PaperDataAPIConverter<Key>(
    {
        Result.success(unwrap().getData(DataComponentTypes.ITEM_MODEL)?.toAPI())
    }, {
        unwrap().setData(DataComponentTypes.ITEM_MODEL, it.into())
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.ITEM_MODEL)
        Result.success(this to true)
    }
)

internal val itemLoreConverter = PaperDataAPIConverter(
    {
        Result.success(unwrap().getData(DataComponentTypes.LORE)?.lines()?.let { ItemLore(it) })
    }, {
        unwrap().setData(DataComponentTypes.LORE, io.papermc.paper.datacomponent.item.ItemLore.lore(it.lines))
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.LORE)
        Result.success(this to true)
    }
)