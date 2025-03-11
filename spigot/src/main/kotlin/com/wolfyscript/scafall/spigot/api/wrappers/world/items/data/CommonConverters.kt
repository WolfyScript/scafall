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
import com.wolfyscript.scafall.wrappers.world.items.data.*
import org.bukkit.Bukkit
import org.bukkit.Registry
import org.bukkit.block.DecoratedPot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.*
import org.bukkit.inventory.meta.Repairable

internal val unbreakableItemMetaConverter = ItemMetaDataKeyConverter<Unbreakable>({
    if (isUnbreakable) {
        return@ItemMetaDataKeyConverter Unbreakable(hasItemFlag(ItemFlag.HIDE_UNBREAKABLE))
    }
    null
}, { data ->
    isUnbreakable = data != null
    if (data?.showInTooltip == true) {
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
            damage = it ?: 0
        }
    }
)

internal val maxDamageItemMetaConverter = ItemMetaDataKeyConverter<Int>({
    if (this is Damageable) {
        if (hasMaxDamage()) {
            return@ItemMetaDataKeyConverter maxDamage
        }
    }
    null
}, {
    if (this is Damageable) {
        this.setMaxDamage(it)
    }
})

internal val maxStackSizeItemMetaConverter = ItemMetaDataKeyConverter<Int>({
    if (hasMaxStackSize()) {
        return@ItemMetaDataKeyConverter maxStackSize
    }
    null
}, {
    if (it != null) {
        setMaxStackSize(it)
    } else {
        setMaxStackSize(null)
    }
})

internal val repairCostItemMetaConverter = ItemMetaDataKeyConverter({
    if (this !is Repairable) return@ItemMetaDataKeyConverter null
    this.repairCost
}, {
    if (this !is Repairable) return@ItemMetaDataKeyConverter
    repairCost = it ?: 0
})

internal val mapIdItemMetaConverter = ItemMetaDataKeyConverter({
    if (this !is MapMeta || !hasMapId()) return@ItemMetaDataKeyConverter null
    mapId
}, {
    if (this is MapMeta) {
        if (it != null) {
            val map = Bukkit.getMap(it)
            if (map != null) {
                mapView = map
            }
        } else {
            mapView = null
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
        instrument = it?.let { key -> Registry.INSTRUMENT.get(key.bukkit()) }
    }
})

internal val recipesItemMetaConverter = ItemMetaDataKeyConverter({
    if (this is KnowledgeBookMeta) {
        return@ItemMetaDataKeyConverter recipes.map { it.api() }
    }
    null
}, { keys ->
    if (this is KnowledgeBookMeta) {
        recipes = keys?.map { it.bukkit() } ?: emptyList()
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
            state.noteBlockSound = it?.bukkit()
        }
    }
})

internal val baseColorItemMetaConverter = ItemMetaDataKeyConverter({
    if (this is ShieldMeta) {
        return@ItemMetaDataKeyConverter baseColor?.toWrapper()
    }
    null
}, {
    if (this is ShieldMeta) {
        baseColor = it?.toBukkit()
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
    if (this is BlockStateMeta) {
        val state = blockState
        if (state is DecoratedPot) {
            if (it == null) {
                state.setSherd(DecoratedPot.Side.FRONT, null)
                state.setSherd(DecoratedPot.Side.RIGHT, null)
                state.setSherd(DecoratedPot.Side.LEFT, null)
                state.setSherd(DecoratedPot.Side.BACK, null)
                return@ItemMetaDataKeyConverter
            }
            state.setSherd(DecoratedPot.Side.FRONT, Registry.MATERIAL.get(it[0].bukkit()))
            state.setSherd(DecoratedPot.Side.RIGHT, Registry.MATERIAL.get(it[1].bukkit()))
            state.setSherd(DecoratedPot.Side.LEFT, Registry.MATERIAL.get(it[2].bukkit()))
            state.setSherd(DecoratedPot.Side.BACK, Registry.MATERIAL.get(it[3].bukkit()))
        }
    }
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
    setItems(it?.contents?.map { stack -> stack.unwrap() })
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

internal val gliderItemMetaConverter = ItemMetaDataKeyConverter<Glider>({
    if (isGlider) {
        Glider()
    } else null
}, {
    isGlider = it != null
})

internal val hideTooltipItemMetaConverter = ItemMetaDataKeyConverter<HideTooltip>({
    if (isHideTooltip) {
        return@ItemMetaDataKeyConverter HideTooltip()
    }
    null
}, {
    isHideTooltip = it != null
})

internal val hideAdditionalTooltipItemMetaConverter = ItemMetaDataKeyConverter<HideAdditionalTooltip>({
    if (hasItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)) {
        return@ItemMetaDataKeyConverter HideAdditionalTooltip()
    }
    null
}, {
    if (it != null) {
        addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
    } else {
        removeItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
    }
})

