package com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.itemsadder

import com.wolfyscript.scaffolding.ScaffoldingProvider.Companion.get
import com.wolfyscript.scaffolding.spigot.api.into
import com.wolfyscript.scaffolding.spigot.api.persistentStorage
import com.wolfyscript.scaffolding.spigot.platform.listener.PersistentStorageListener
import com.wolfyscript.scaffolding.spigot.platform.persistent.events.BlockStorageBreakEvent
import com.wolfyscript.scaffolding.spigot.platform.persistent.events.BlockStoragePlaceEvent
import com.wolfyscript.scaffolding.spigot.platform.persistent.world.WorldStorage
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.metadata.FixedMetadataValue

class CustomItemListener(private val iaImpl: ItemsAdderImpl) :
    Listener {
    private val core =
        get()

    @EventHandler
    fun onBlockPlacement(event: CustomBlockPlaceEvent) {
        /*
        Call the StoragePlaceEvent when placing IA blocks!
         */
        if (event.isCanBuild) {
            val block = event.block
            val worldStorage: WorldStorage = core.persistentStorage.getOrCreateWorldStorage(block.world)
            val blockStorage = worldStorage.createBlockStorage(block.location)
            val blockStorePlaceEvent = BlockStoragePlaceEvent(
                block,
                blockStorage,
                event.replacedBlockState,
                event.placedAgainst,
                event.itemInHand,
                event.player,
                event.isCanBuild,  // Since we do not get the hand used, we need to guess it.
                if (event.itemInHand.isSimilar(event.player.equipment.itemInMainHand)) EquipmentSlot.HAND else EquipmentSlot.OFF_HAND
            )
            blockStorePlaceEvent.isCancelled = event.isCancelled
            Bukkit.getPluginManager().callEvent(blockStorePlaceEvent)
            event.isCancelled = blockStorePlaceEvent.isCancelled

            if (!blockStorage.isEmpty && !blockStorePlaceEvent.isCancelled) {
                worldStorage.setBlockStorageIfAbsent(blockStorage)
            }
        }
    }

    @EventHandler
    fun onBlockBreak(event: CustomBlockBreakEvent) {
        /*
        Doesn't look like this is actually required. Include it anyway just in case as it is just called if the stored data still exists!
         */
        val block = event.block
        val worldStorage = core.persistentStorage.getOrCreateWorldStorage(block.world)
        worldStorage.getBlock(block.location).ifPresent { store ->
            if (!store.isEmpty) {
                val blockStorageBreakEvent =
                    BlockStorageBreakEvent(
                        event.block,
                        store,
                        event.player
                    )
                blockStorageBreakEvent.isCancelled = event.isCancelled
                Bukkit.getPluginManager().callEvent(blockStorageBreakEvent)
                if (blockStorageBreakEvent.isCancelled) return@ifPresent
                worldStorage.removeBlock(block.location).ifPresent { storage ->
                    event.block
                        .setMetadata(PersistentStorageListener.PREVIOUS_BROKEN_STORE, FixedMetadataValue(core.corePlugin.into().plugin, storage))
                }
            }
        }
    }
}
