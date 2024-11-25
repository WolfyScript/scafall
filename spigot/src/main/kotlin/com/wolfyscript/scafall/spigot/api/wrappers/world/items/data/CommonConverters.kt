package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.toBukkit
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.toWrapper
import com.wolfyscript.scafall.spigot.api.wrappers.wrap
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.BundleContents
import com.wolfyscript.scafall.wrappers.world.items.data.Lock
import com.wolfyscript.scafall.wrappers.world.items.data.Unbreakable
import org.bukkit.Bukkit
import org.bukkit.Registry
import org.bukkit.block.Banner
import org.bukkit.block.DecoratedPot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.*

internal val unbreakableItemMetaConverter = ItemMetaDataKeyConverter<Unbreakable>({
    if (isUnbreakable) {
        return@ItemMetaDataKeyConverter Unbreakable(hasItemFlag(ItemFlag.HIDE_UNBREAKABLE))
    }
    null
}, { data ->
    isUnbreakable = true
    if (data.showInTooltip) {
        addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
    } else {
        removeItemFlags(ItemFlag.HIDE_UNBREAKABLE)
    }
})

internal val damageItemMetaConverter = ItemMetaDataKeyConverter(
    {
        return@ItemMetaDataKeyConverter if (this is Damageable) {
            damage
        } else {
            null
        }
    }, {
        if (this is Damageable) {
            damage = it
        }
    }
)

internal val repairCostItemMetaConverter = ItemMetaDataKeyConverter({
    if (this !is Repairable) return@ItemMetaDataKeyConverter null
    this.repairCost
}, {
    if (this !is Repairable) return@ItemMetaDataKeyConverter
    repairCost = it
})

internal val mapIdItemMetaConverter = ItemMetaDataKeyConverter({
    if (this !is MapMeta || !hasMapId()) return@ItemMetaDataKeyConverter null
    mapId
}, {
    if (this is MapMeta) {
        val map = Bukkit.getMap(it)
        if (map != null) {
            mapView = map
        }
    }
})

internal val instrumentItemMetaConverter = ItemMetaDataKeyConverter({
    if (this is MusicInstrumentMeta) {
        return@ItemMetaDataKeyConverter instrument?.key?.api()
    }
    null
}, {
    if (this is MusicInstrumentMeta) {
        instrument = Registry.INSTRUMENT.get(it.bukkit())
    }
})

internal val recipesItemMetaConverter = ItemMetaDataKeyConverter({
    if (this is KnowledgeBookMeta) {
        return@ItemMetaDataKeyConverter recipes.map { it.api() }
    }
    null
}, { keys ->
    if (this is KnowledgeBookMeta) {
        recipes = keys.map { it.bukkit() }
    }
})

internal val noteBlockSoundItemMetaConverter = ItemMetaDataKeyConverter({
    if (this is BlockStateMeta) {
        val state = blockState
        if (state is SkullMeta) {
            return@ItemMetaDataKeyConverter state.noteBlockSound?.api()
        }
    }
    null
}, {
    if (this is BlockStateMeta) {
        val state = blockState
        if (state is SkullMeta) {
            state.noteBlockSound = it.bukkit()
        }
    }
})

internal val baseColorItemMetaConverter = ItemMetaDataKeyConverter({
    if (this is BlockStateMeta) {
        val state = this.blockState
        if (state is Banner) {
            return@ItemMetaDataKeyConverter state.baseColor.toWrapper()
        }
    }
    null
}, {
    if (this is BlockStateMeta) {
        val state = blockState
        if (state is Banner) {
            state.baseColor = it.toBukkit()
        }
        blockState = state
    }
})

internal val potDecorationsItemMetaConverter = ItemMetaDataKeyConverter({
    if (this is BlockStateMeta) {
        // Why? A BlockState? really Spigot? How does it work together with the block_state data component?
        val state = blockState
        if (state is DecoratedPot) {
            // north, west, east, south // when placed facing north
            // The data component internally just uses a list of sherds
            return@ItemMetaDataKeyConverter buildList<Key> {
                // Assumed based on facing direction when placed down
                add(state.sherds[DecoratedPot.Side.FRONT]!!.key.toAPI())
                add(state.sherds[DecoratedPot.Side.RIGHT]!!.key.toAPI())
                add(state.sherds[DecoratedPot.Side.LEFT]!!.key.toAPI())
                add(state.sherds[DecoratedPot.Side.BACK]!!.key.toAPI())
            }
        }
    }
    null
}, {

})

internal val lockItemMetaConverter = ItemMetaDataKeyConverter<Lock>({
    TODO("Not implemented yet!")
}, {
    TODO("Not implemented yet!")
})

internal val bundleContentsItemMetaConverter = ItemMetaDataKeyConverter({
    if (this is BundleMeta) {
        return@ItemMetaDataKeyConverter BundleContents(items.map { it.wrap() })
    }
    return@ItemMetaDataKeyConverter BundleContents(emptyList())
}, {
    if (this !is BundleMeta) return@ItemMetaDataKeyConverter
    setItems(it.contents.map { stack -> stack.unwrap() })
})

internal val enchantmentGlintOverrideItemMetaConverter =
    ItemMetaDataKeyConverter({
        if (hasEnchantmentGlintOverride()) {
            true
        } else {
            null
        }
    }, {
        setEnchantmentGlintOverride(it)
    })


