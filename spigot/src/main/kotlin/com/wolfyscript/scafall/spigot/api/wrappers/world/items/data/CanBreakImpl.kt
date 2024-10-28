package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.wrappers.world.items.data.CanBreak
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag

internal val canBreakItemMetaConverter = ItemMetaDataKeyConverter<CanBreak>({
    val show = !hasItemFlag(ItemFlag.HIDE_PLACED_ON)
    return@ItemMetaDataKeyConverter if (ScafallProvider.get().platformType == PlatformType.PAPER) {
        CanBreakImpl(show, destroyableKeys.map { Key.key(it.namespace, it.key) })
    } else {
        CanBreakImpl(show, canDestroy.map { it.key.api() })
    }
}, { canBreak ->
    if (ScafallProvider.get().platformType == PlatformType.PAPER) {
        setDestroyableKeys(canBreak.blocks.map { it.bukkit() })
    } else {
        /*
        * WARNING: Possible LOSS of Information!
        *
        * Keys that are not valid materials are lost!
        * */
        canDestroy = canBreak.blocks.map { Material.getMaterial(it.toString()) }.toSet()
    }
})

class CanBreakImpl(override val showInTooltip: Boolean, override val blocks: List<Key>) : CanBreak