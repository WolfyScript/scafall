package com.wolfyscript.scaffolding.spigot.platform.listener.custom_item

import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.spigot.api.persistentStorage
import com.wolfyscript.scaffolding.spigot.platform.persistent.events.BlockStorageBreakEvent
import com.wolfyscript.scaffolding.spigot.platform.persistent.events.BlockStorageDropItemsEvent
import com.wolfyscript.scaffolding.spigot.platform.persistent.events.BlockStorageMultiPlaceEvent
import com.wolfyscript.scaffolding.spigot.platform.persistent.events.BlockStoragePlaceEvent
import com.wolfyscript.scaffolding.spigot.platform.persistent.world.BlockStorage
import com.wolfyscript.scaffolding.spigot.platform.persistent.world.ChunkStorage
import com.wolfyscript.scaffolding.spigot.platform.world.items.CustomItem
import com.wolfyscript.scaffolding.spigot.platform.world.items.CustomItem.Companion.getByItemStack
import com.wolfyscript.scaffolding.spigot.platform.world.items.CustomItemBlockData
import com.wolfyscript.scaffolding.spigot.platform.world.items.ItemUtils.isAirOrNull
import com.wolfyscript.scaffolding.spigot.platform.world.items.events.CustomItemPlaceEvent
import org.bukkit.Bukkit
import org.bukkit.block.Container
import org.bukkit.block.ShulkerBox
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.meta.BlockStateMeta
import java.util.function.Consumer

class CustomItemDataListener(private val core: Scaffolding) : Listener {
    @EventHandler
    fun onDropItems(event: BlockStorageDropItemsEvent) {
        event.storage.getData(
            CustomItemBlockData.ID,
            CustomItemBlockData::class.java
        ).ifPresent { data: CustomItemBlockData ->
            val blockState = event.blockState
            data.customItem.ifPresent { customItem: CustomItem ->
                if (customItem.blockSettings.isUseCustomDrops) {
                    event.isCancelled = true
                    //TODO for future: Let people customize this!
                    val result = customItem.create()
                    if (blockState is Container) {
                        val blockStateMeta = result.itemMeta as BlockStateMeta
                        if (blockState is ShulkerBox) {
                            val shulkerBox = blockStateMeta.blockState as ShulkerBox
                            shulkerBox.inventory.contents = blockState.getInventory().contents
                            blockStateMeta.blockState = shulkerBox
                        } else {
                            val itemContainer = blockStateMeta.blockState as Container
                            itemContainer.inventory.clear()
                            blockStateMeta.blockState = itemContainer
                        }
                        result.setItemMeta(blockStateMeta)
                    }
                    blockState.world.dropItemNaturally(blockState.location, result)
                }
            }
        }
    }

    @EventHandler
    fun onPlaceBlock(event: BlockStoragePlaceEvent) {
        var customItem = getByItemStack(event.itemInHand)
        if (!isAirOrNull(customItem) && customItem!!.isBlock) {
            if (customItem.isBlockPlacement) {
                event.isCancelled = true
            }
            val event1 = CustomItemPlaceEvent(customItem, event)
            Bukkit.getPluginManager().callEvent(event1)
            customItem = event1.customItem
            if (!event1.isCancelled) {
                if (customItem != null) {
                    val blockLoc = event.blockPlaced.location
                    val chunkStorage: ChunkStorage =
                        core.persistentStorage.getOrCreateWorldStorage(blockLoc.world).getOrCreateChunkStorage(blockLoc)
                    val customItemData = CustomItemBlockData(
                        core, chunkStorage, blockLoc.toVector(), customItem.key()
                    )
                    event.storage.addOrSetData(customItemData)
                    customItemData.onPlace(event)
                }
            } else {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onMultiPlaceBlock(event: BlockStorageMultiPlaceEvent) {
        val customItem = getByItemStack(event.itemInHand)
        if (!isAirOrNull(customItem)) {
            if (customItem!!.isBlockPlacement) {
                event.isCancelled = true
                return
            }
            event.blockStorages.forEach { blockStorage ->
                val blockLoc = event.blockPlaced.location
                val chunkStorage: ChunkStorage =
                    core.persistentStorage.getOrCreateWorldStorage(blockLoc.world).getOrCreateChunkStorage(blockLoc)
                val customItemData = CustomItemBlockData(
                    core, chunkStorage, blockLoc.toVector(), customItem.key()
                )
                blockStorage.addOrSetData(customItemData)
                customItemData.onPlace(event)
            }
        }
    }

    @EventHandler
    fun onBreakBlock(event: BlockStorageBreakEvent) {
        event.storage.getData(
            CustomItemBlockData.ID,
            CustomItemBlockData::class.java
        ).ifPresent { data: CustomItemBlockData ->
            data.onBreak(event)
        }
    }
}
