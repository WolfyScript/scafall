package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.wrappers.world.items.data.ItemLore
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer

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
