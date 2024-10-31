package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.wrappers.world.items.data.CanPlaceOn
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag

internal val canPlaceOnItemMetaConverter = ItemMetaDataKeyConverter<CanPlaceOn>({
    val show = !hasItemFlag(ItemFlag.HIDE_PLACED_ON)
    return@ItemMetaDataKeyConverter if (ScafallProvider.get().platformType == PlatformType.PAPER) {
        CanPlaceOn(show, placeableKeys.map { Key.key(it.namespace, it.key) })
    } else {
        CanPlaceOn(show, canPlaceOn.map { it.key.api() })
    }
}, { placeOn ->
    if (ScafallProvider.get().platformType == PlatformType.PAPER) {
        setPlaceableKeys(placeOn.blocks.map { it.bukkit() })
    } else {
        /*
        * WARNING: Possible LOSS of Information!
        *
        * Keys that are not valid materials are lost!
        * */
        canPlaceOn = placeOn.blocks.map { Material.getMaterial(it.toString()) }.toSet()
    }
})
