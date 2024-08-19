package com.wolfyscript.scafall.spigot.platform.listener

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.spigot.api.into
import com.wolfyscript.scafall.spigot.api.persistentStorage
import com.wolfyscript.scafall.spigot.platform.persistent.PersistentStorage
import com.wolfyscript.scafall.spigot.platform.persistent.events.BlockStorageBreakEvent
import com.wolfyscript.scafall.spigot.platform.persistent.events.BlockStorageDropItemsEvent
import com.wolfyscript.scafall.spigot.platform.persistent.events.BlockStorageMultiPlaceEvent
import com.wolfyscript.scafall.spigot.platform.persistent.events.BlockStoragePlaceEvent
import com.wolfyscript.scafall.spigot.platform.persistent.world.BlockStorage
import com.wolfyscript.scafall.spigot.platform.persistent.world.WorldStorage
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.BlockState
import org.bukkit.block.data.Bisected
import org.bukkit.block.data.type.Bed
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.ServerLoadEvent
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.event.world.ChunkUnloadEvent
import org.bukkit.event.world.WorldSaveEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue
import org.bukkit.util.Vector
import java.util.function.Consumer

class PersistentStorageListener(private val core: Scafall) : Listener {
    private val persistentStorage: PersistentStorage = core.persistentStorage

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        val playerStorage = persistentStorage.getOrCreatePlayerStorage(event.player)
        playerStorage.updateAndClearCache() // Clear cache when player leaves the server, to not waste memory!
    }

    @EventHandler
    private fun onChunkLoad(event: ChunkLoadEvent) {
        val chunk = event.chunk
        initOrUpdateChunk(chunk)
    }

    @EventHandler
    private fun onChunkUnload(event: ChunkUnloadEvent) {
        val chunk = event.chunk
        val worldStorage = persistentStorage.getOrCreateWorldStorage(event.world)
        val chunkStorage = worldStorage.getOrCreateChunkStorage(chunk.x, chunk.z)
        chunkStorage.storedBlocks.forEach { (vector: Vector?, store: BlockStorage?) ->
            store.onUnload()
        }
        worldStorage.unloadChunk(chunkStorage)
    }

    @EventHandler
    private fun onSave(event: WorldSaveEvent) {
        val world = event.world
    }

    /**
     * This is required since the world was loaded before the plugin was enabled, therefor the spawn-chunks are already loaded.
     * So this makes sure to initialize the existing chunks.
     */
    @EventHandler
    private fun onServerLoad(event: ServerLoadEvent) {
        for (world in Bukkit.getWorlds()) {
            for (chunk in world.loadedChunks) {
                initOrUpdateChunk(chunk)
            }
        }
    }

    private fun initOrUpdateChunk(chunk: Chunk) {
        val chunkStorage =
            persistentStorage.getOrCreateWorldStorage(chunk.world).getOrCreateChunkStorage(chunk.x, chunk.z)
        chunkStorage.loadBlocksIntoCache()
        chunkStorage.storedBlocks.forEach { (vector: Vector?, blockStorage: BlockStorage?) -> blockStorage.onLoad() }
    }

    /* ******************** *
     * Block Storage events *
     * ******************** */
    /**
     * Handles the BlockStorages that are placed together with blocks.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockStoragePlace(event: BlockPlaceEvent) {
        if (event.canBuild()) {
            val block = event.block
            val worldStorage = persistentStorage.getOrCreateWorldStorage(block.world)
            val blockStorage = worldStorage.createBlockStorage(block.location)
            val blockStorePlaceEvent = BlockStoragePlaceEvent(
                block,
                blockStorage,
                event.blockReplacedState,
                event.blockAgainst,
                event.itemInHand,
                event.player,
                event.canBuild(),
                event.hand
            )
            blockStorePlaceEvent.isCancelled = event.isCancelled
            Bukkit.getPluginManager().callEvent(blockStorePlaceEvent)
            event.isCancelled = blockStorePlaceEvent.isCancelled

            if (!blockStorage.isEmpty && !blockStorePlaceEvent.isCancelled) {
                worldStorage.setBlockStorageIfAbsent(blockStorage)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockPlaceMulti(event: BlockMultiPlaceEvent) {
        val worldStorage = persistentStorage.getOrCreateWorldStorage(event.block.world)
        val storages = event.replacedBlockStates.stream()
            .map { state: BlockState -> worldStorage.createBlockStorage(state.location) }.toList()
        val blockStorageMultiPlaceEvent = BlockStorageMultiPlaceEvent(
            event.replacedBlockStates,
            storages,
            event.blockAgainst,
            event.itemInHand,
            event.player,
            event.canBuild(),
            event.hand
        )
        blockStorageMultiPlaceEvent.isCancelled = event.isCancelled
        Bukkit.getPluginManager().callEvent(blockStorageMultiPlaceEvent)
        event.isCancelled = blockStorageMultiPlaceEvent.isCancelled

        if (blockStorageMultiPlaceEvent.isCancelled) return

        for (i in blockStorageMultiPlaceEvent.blockStorages.indices) {
            val blockStorage = blockStorageMultiPlaceEvent.blockStorages[i]
            if (!blockStorage.isEmpty) {
                worldStorage.setBlockStorageIfAbsent(blockStorage)
            }
        }
    }

    /**
     * Called when liquid flows or when a Dragon Egg teleports.
     * This Listener only listens for the Dragon Egg.
     * The BlockStorage is copied from the original position to the new postion.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onBlockFromTo(event: BlockFromToEvent) {
        val block = event.block
        val worldStore = persistentStorage.getOrCreateWorldStorage(event.block.world)
        worldStore.getBlock(block.location).ifPresent { store: BlockStorage ->
            val loc = event.toBlock.location
            worldStore.removeBlock(block.location)
            store.copyToOtherBlockStorage(worldStore.getOrCreateAndSetBlockStorage(loc))
        }
    }

    /**
     * Makes sure that the positions of BlockStorages are updated correctly when pushed by a piston.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPistonExtend(event: BlockPistonExtendEvent) {
        updatePistonBlocks(event.block.world, event.blocks, event.direction)
    }

    /**
     * Makes sure that the positions of BlockStorages are updated correctly when pulled by a piston.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPistonRetract(event: BlockPistonRetractEvent) {
        updatePistonBlocks(event.block.world, event.blocks, event.direction)
    }

    private fun updatePistonBlocks(world: World, blocks: List<Block>, direction: BlockFace) {
        val worldStorage = persistentStorage.getOrCreateWorldStorage(world)
        blocks.forEach(Consumer { block: Block ->
            worldStorage.getBlock(block.location).ifPresent { store: BlockStorage ->
                val moveTo = block.getRelative(direction).location
                worldStorage.removeBlock(block.location)
                store.copyToOtherBlockStorage(worldStorage.getOrCreateAndSetBlockStorage(moveTo))
            }
        })
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockStorageBreak(event: BlockBreakEvent) {
        val block = event.block
        val worldStorage = persistentStorage.getOrCreateWorldStorage(block.world)
        worldStorage.getBlock(block.location).ifPresent { store: BlockStorage ->
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
                worldStorage.removeBlock(block.location)
                    .ifPresent { storage: BlockStorage? ->
                        if (event.isDropItems) {
                            event.block.setMetadata(
                                PREVIOUS_BROKEN_STORE, FixedMetadataValue(
                                    core.corePlugin.into().plugin, storage
                                )
                            )
                        }
                    }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onEntityExplodeBlockStorages(event: EntityExplodeEvent) {
        event.isCancelled =
            handleExplodedBlockStorages(
                persistentStorage.getOrCreateWorldStorage(event.entity.world),
                event.blockList()
            )
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onBlockExplodeStorages(event: BlockExplodeEvent) {
        val worldStorage = persistentStorage.getOrCreateWorldStorage(event.block.world)
        worldStorage.removeBlock(event.block.location) // Remove the block that exploded, since that might have had custom data.
        //event.setYield(0f);
        event.isCancelled = handleExplodedBlockStorages(worldStorage, event.blockList())
    }

    private fun handleExplodedBlockStorages(worldStorage: WorldStorage, blocks: MutableList<Block>): Boolean {
        val cancel = blocks.stream().anyMatch { block: Block -> worldStorage.removeBlock(block.location).isPresent }
        if (cancel) {
            // Only use the custom behaviour if a block storage was included in the explosion
            val blockIterator = blocks.iterator()
            while (blockIterator.hasNext()) {
                val block = blockIterator.next()
                val location = block.location
                blockIterator.remove()
                val itemStacks = block.drops
                val state = block.state
                block.type = Material.AIR
                // Handle custom block storage drops, if available
                worldStorage.getOrCreateChunkStorage(location).getBlock(location)
                    .ifPresentOrElse({ storage: BlockStorage? ->
                        val world = location.world
                        if (world != null) {
                            val itemDrops = itemStacks.stream().map { itemStack: ItemStack? ->
                                world.dropItemNaturally(
                                    location,
                                    itemStack!!
                                )
                            }.toList()
                            // Call the drop event for plugins to manipulate the drops
                            val blockStoreDropItemsEvent = BlockStorageDropItemsEvent(
                                block, state,
                                storage!!, null, ArrayList(itemDrops)
                            )
                            Bukkit.getPluginManager().callEvent(blockStoreDropItemsEvent)
                            val eventItems = blockStoreDropItemsEvent.items
                            if (blockStoreDropItemsEvent.isCancelled) {
                                blockStoreDropItemsEvent.items
                            }
                            //Remove the items that were removed from the list
                            itemDrops.stream().filter { item: Item -> !eventItems.contains(item) }
                                .forEach { obj: Item -> obj.remove() }
                        }
                    }, {
                        // Otherwise just drop the items
                        val world = location.world
                        if (world != null) {
                            itemStacks.forEach(Consumer { itemStack: ItemStack? ->
                                world.dropItemNaturally(
                                    location,
                                    itemStack!!
                                )
                            })
                        }
                    })
            }
            return true
        }
        return false
    }

    private fun removeMultiBlockItems(block: Block) {
        val worldStorage = persistentStorage.getOrCreateWorldStorage(block.world)
        when (val blockData = block.blockData) {
            is Bisected -> {
                worldStorage.removeBlock(
                    if (blockData.half == Bisected.Half.BOTTOM) {
                        block.location.add(0.0, 1.0, 0.0)
                    } else {
                        block.location.subtract(0.0, 1.0, 0.0)
                    }
                )
            }
            is Bed -> {
                worldStorage.removeBlock(block.location.add(blockData.facing.getDirection()))
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockStorageItemDrop(event: BlockDropItemEvent) {
        val state = event.blockState // Get previous state with old metadata.
        state.getMetadata(PREVIOUS_BROKEN_STORE).stream()
            .filter { metadataValue: MetadataValue -> metadataValue.owningPlugin == core }.findFirst()
            .ifPresent { metadataValue: MetadataValue ->
                event.block.removeMetadata(
                    PREVIOUS_BROKEN_STORE,
                    core.corePlugin.into().plugin
                ) //Remove old metadata from block!
                val store = metadataValue.value()
                if (store is BlockStorage) {
                    val blockStoreDropItemsEvent = BlockStorageDropItemsEvent(event.block, state, store, event.player, event.items)
                    Bukkit.getPluginManager().callEvent(blockStoreDropItemsEvent)
                    event.isCancelled = blockStoreDropItemsEvent.isCancelled
                }
            }
    }

    /**
     * Removes the BlockStorage if the block is burned.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onBlockBurn(event: BlockBurnEvent) {
        removeIfAvailable(event.block)
    }

    /**
     * Removes the BlockStorage if the block is a leave and decays.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onLeavesDecay(event: LeavesDecayEvent) {
        removeIfAvailable(event.block)
    }

    /**
     * Removes the BlockStorage if the block disappears because of world conditions.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onBlockFade(event: BlockFadeEvent) {
        if (event.newState.type == Material.AIR) {
            removeIfAvailable(event.block)
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onBlockPhysics(event: BlockPhysicsEvent) {
        if (event.changedType == Material.AIR) {
            removeIfAvailable(event.block)
        }
    }

    private fun removeIfAvailable(block: Block) {
        val worldStorage = persistentStorage.getOrCreateWorldStorage(block.world)
        worldStorage.getBlock(block.location).ifPresent {
            worldStorage.removeBlock(block.location)
        }
    }

    companion object {
        const val PREVIOUS_BROKEN_STORE: String = "previous_store"
    }
}
