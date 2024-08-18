package com.wolfyscript.scaffolding.spigot.api.wrappers.world.items.data

import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.spigot.api.identifiers.bukkit
import com.wolfyscript.scaffolding.spigot.api.identifiers.api
import com.wolfyscript.scaffolding.wrappers.world.items.data.CanBreak
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag

class CanBreakImpl(override val showInTooltip: Boolean, private val blocks: List<Key>) : CanBreak {

    companion object {
        internal val ITEM_META_CONVERTER = ItemMetaDataKeyConverter<CanBreak>({
            val show = !hasItemFlag(ItemFlag.HIDE_PLACED_ON)
            return@ItemMetaDataKeyConverter if (false/*TODO: check if paper*/) {
                CanBreakImpl(show, destroyableKeys.map { Key.key(it.namespace, it.key) })
            } else {
                CanBreakImpl(show, canDestroy.map { it.key.api() })
            }
        }, { canBreak ->
            if (false/*TODO: check if paper*/) {
                setDestroyableKeys(canBreak.blocks().map { it.bukkit() })
            } else {
                /*
                * WARNING: Possible LOSS of Information!
                *
                * Keys that are not valid materials are lost!
                * */
                canDestroy = canBreak.blocks().map { Material.getMaterial(it.toString()) }.toSet()
            }
        })
    }

    override fun blocks(): List<Key> = blocks
}