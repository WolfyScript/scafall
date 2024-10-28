package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.ItemStackImpl
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.toBukkit
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.toWrapper
import com.wolfyscript.scafall.wrappers.world.items.data.BundleContents
import com.wolfyscript.scafall.wrappers.world.items.data.Unbreakable
import org.bukkit.Bukkit
import org.bukkit.Registry
import org.bukkit.block.Banner
import org.bukkit.block.DecoratedPot
import org.bukkit.block.Lockable
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

internal val displayNameItemMetaConverter = ItemMetaDataKeyConverter({ displayName() }, { data -> displayName(data) })

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

internal val customModelDataItemMetaConverter = ItemMetaDataKeyConverter({
    return@ItemMetaDataKeyConverter if (hasCustomModelData()) {
        customModelData
    } else null
}, {
    setCustomModelData(it)
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
        val state = blockState
        if (state is DecoratedPot) {
            val sherds = state.shards
            return@ItemMetaDataKeyConverter sherds.map {
                it.key.api()
            }
            /*
        buildList<NamespacedKey> {
            // TODO: What is the order of shards here?
            add(BukkitNamespacedKey.fromBukkit(sherds[DecoratedPot.Side.FRONT]!!.key))
            add(BukkitNamespacedKey.fromBukkit(sherds[DecoratedPot.Side.LEFT]!!.key))
            add(BukkitNamespacedKey.fromBukkit(sherds[DecoratedPot.Side.BACK]!!.key))
            add(BukkitNamespacedKey.fromBukkit(sherds[DecoratedPot.Side.RIGHT]!!.key))
        }
             */
        }
    }
    null
}, {

})

internal val lockItemMetaConverter = ItemMetaDataKeyConverter({
    if (this is BlockStateMeta) {
        val state = blockState
        if (state is Lockable) {
            return@ItemMetaDataKeyConverter state.lock
        }
    }
    null
}, {
    if (this !is BlockStateMeta) return@ItemMetaDataKeyConverter
    val state = blockState
    if (state !is Lockable) return@ItemMetaDataKeyConverter
    state.setLock(it)
    blockState = state
})

internal val bundleContentsItemMetaConverter = ItemMetaDataKeyConverter({
    if (this is BundleMeta) {
        return@ItemMetaDataKeyConverter BundleContents(items.map { ItemStackImpl(it) })
    }
    return@ItemMetaDataKeyConverter BundleContents(emptyList())
}, {
    if (this !is BundleMeta) return@ItemMetaDataKeyConverter
    setItems(it.contents.map { stack -> (stack as ItemStackImpl).bukkitRef })
})

internal val enchantmentGlintOverrideItemMetaConverter =
    ItemMetaDataKeyConverter<Boolean>({
        TODO("Not yet implemented")
    }, {
        TODO("Not yet implemented")
    })


