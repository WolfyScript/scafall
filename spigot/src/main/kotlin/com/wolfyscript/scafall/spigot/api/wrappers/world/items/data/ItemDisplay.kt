package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.ItemLore
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer
import net.kyori.adventure.text.Component

internal val itemLoreItemMetaConverter =
    if (ScafallProvider.get().platformType == PlatformType.PAPER) {
        ItemMetaDataKeyConverter<ItemLore>(
            {
                if (hasLore()) {
                    ItemLore(lore()?.toList() ?: emptyList())
                }
                null
            },
            {
                lore(it.lines)
            })
    } else {
        ItemMetaDataKeyConverter<ItemLore>(
            {
                if (hasLore()) {
                    // Bukkit has no direct adventure support, so we need to convert the string with legacy color formatting
                    ItemLore(lore?.map { string -> BukkitComponentSerializer.legacy().deserialize(string) } ?: emptyList())
                }
                null
            },
            {
                lore = it.lines.map { component -> BukkitComponentSerializer.legacy().serialize(component) }
            })
    }

internal val customNameItemMetaConverter = ItemMetaDataKeyConverter({ displayName() }, { data -> displayName(data) })

internal val customModelDataItemMetaConverter = ItemMetaDataKeyConverter({
    return@ItemMetaDataKeyConverter if (hasCustomModelData()) {
        customModelData
    } else null
}, {
    setCustomModelData(it)
})

internal val itemNameItemMetaConverter = if (ScafallProvider.get().platformType == PlatformType.PAPER) {
    ItemMetaDataKeyConverter<Component>({
        if (hasItemName()) {
            return@ItemMetaDataKeyConverter itemName()
        }
        null
    }, {
        itemName(it)
    })
} else {
    ItemMetaDataKeyConverter<Component>({
        BukkitComponentSerializer.legacy().deserialize(itemName)
    }, {
        setItemName(BukkitComponentSerializer.legacy().serialize(it))
    })
}

internal val itemModelItemMetaConverter = ItemMetaDataKeyConverter<Key>({
    if (hasItemModel()) {
        return@ItemMetaDataKeyConverter itemModel?.toAPI()
    }
    null
}, {
    itemModel = it.bukkit()
})
