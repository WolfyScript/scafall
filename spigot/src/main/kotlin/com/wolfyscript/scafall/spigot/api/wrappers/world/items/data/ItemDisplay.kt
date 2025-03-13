package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.wrap
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.CustomModelData
import com.wolfyscript.scafall.wrappers.world.items.data.ItemLore
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer
import net.kyori.adventure.text.Component
import org.bukkit.inventory.meta.components.CustomModelDataComponent

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
                lore(it?.lines)
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
                lore = it?.lines?.map { component -> BukkitComponentSerializer.legacy().serialize(component) }
            })
    }

internal val customNameItemMetaConverter = ItemMetaDataKeyConverter({ displayName() }, { data -> displayName(data) })

internal val customModelDataItemMetaConverter = ItemMetaDataKeyConverter({
    if (!hasCustomModelData()) {
        return@ItemMetaDataKeyConverter null
    }
    return@ItemMetaDataKeyConverter CustomModelData(customModelDataComponent.floats, customModelDataComponent.flags, customModelDataComponent.strings, customModelDataComponent.colors.map { it.wrap() })
}, {
    if (it == null) {
        setCustomModelDataComponent(null)
        return@ItemMetaDataKeyConverter
    }
    val data = customModelDataComponent
    data.floats = it.floats
    data.flags = it.flags
    data.strings = it.strings
    data.colors = it.colors.map { it.unwrap() }

    setCustomModelDataComponent(data)
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
        setItemName(it?.let { component -> BukkitComponentSerializer.legacy().serialize(component) })
    })
}

internal val itemModelItemMetaConverter = ItemMetaDataKeyConverter<Key>({
    if (hasItemModel()) {
        return@ItemMetaDataKeyConverter itemModel?.toAPI()
    }
    null
}, {
    itemModel = it?.bukkit()
})
