package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.wrap
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.CustomModelData
import com.wolfyscript.scafall.wrappers.world.items.data.ItemLore
import com.wolfyscript.scafall.wrappers.world.items.data.TooltipDisplay
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey

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

internal val customModelDataConverter = PaperDataAPIConverter(
    {
        Result.success(
            unwrap().getData(DataComponentTypes.CUSTOM_MODEL_DATA)
                ?.let { CustomModelData(it.floats(), it.flags(), it.strings(), it.colors().map { it.wrap() }) })
    }, {
        unwrap().setData(
            DataComponentTypes.CUSTOM_MODEL_DATA,
            io.papermc.paper.datacomponent.item.CustomModelData.customModelData().addFloats(it.floats)
                .addFlags(it.flags).addStrings(it.strings).addColors(it.colors.map { it.unwrap() }).build()
        )
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.CUSTOM_MODEL_DATA)
        Result.success(this to true)
    }
)

internal val tooltipStyleConverter = PaperDataAPIConverter(
    {
        Result.success(unwrap().getData(DataComponentTypes.TOOLTIP_STYLE)?.toAPI())
    }, {
        unwrap().setData(DataComponentTypes.TOOLTIP_STYLE, it.into())
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.TOOLTIP_STYLE)
        Result.success(this to true)
    }
)

internal val tooltipDisplayConverter = PaperDataAPIConverter<TooltipDisplay>(
    {
        val tooltipDisplay = unwrap().getData(DataComponentTypes.TOOLTIP_DISPLAY)
        Result.success(
            if (tooltipDisplay != null) {
                TooltipDisplay(
                    tooltipDisplay.hideTooltip(),
                    tooltipDisplay.hiddenComponents().map { it.key().toAPI() }.toMutableSet()
                )
            } else {
                null
            }
        )
    }, {
        val registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.DATA_COMPONENT_TYPE)
        unwrap().setData(
            DataComponentTypes.TOOLTIP_DISPLAY,
            io.papermc.paper.datacomponent.item.TooltipDisplay.tooltipDisplay().hideTooltip(it.displayed)
                .hiddenComponents(it.hiddenComponents.mapNotNull { registry.get(it.into()) }.toSet())
        )
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.TOOLTIP_DISPLAY)
        Result.success(this to true)
    }
)
