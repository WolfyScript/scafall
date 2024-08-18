package com.wolfyscript.scaffolding.spigot.platform.persistent.world

import com.fasterxml.jackson.annotation.JsonIncludeProperties
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.math.Vec2i
import com.wolfyscript.scaffolding.spigot.platform.persistent.world.BlockStorage.PersistentType
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Collectors

@JsonIncludeProperties
class ChunkStorage private constructor(
    /**
     * Gets the parent WorldStorage of this ChunkStorage.
     *
     * @return The parent WorldStorage.
     */
    val worldStorage: WorldStorage, private val coords: Vec2i
) {
    private val BLOCKS: MutableMap<Vector, BlockStorage> = HashMap()

    @JvmField
    val core: Scaffolding = worldStorage.core

    /**
     * Loads the blocks from the PersistentDataContainer into the cache.<br></br>
     * From this point on the cache and PersistentDataContainer is kept in sync whenever adding/removing blocks.<br></br>
     * <br></br>
     * **If for whatever reason the PersistentDataContainer was modified, this method should be called to update the cache!**
     */
    fun loadBlocksIntoCache() {
        persistentBlocksContainer?.let { blocks ->
            blocks.keys.forEach(
                Consumer { key: NamespacedKey ->
                    val coordsStrings = key.key.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val coords = IntArray(3)
                    for (i in coordsStrings.indices) {
                        coords[i] = coordsStrings[i].toInt()
                    }
                    val coordsVec = Vector(coords[0], coords[1], coords[2])
                    setBlockStorageIfAbsent(
                        blocks.get(key, PersistentType(this, coordsVec))!!
                    )
                })
        }
    }

    val chunk: Chunk?
        /**
         * Gets the Chunk this Storage belongs to.<br></br>
         * **This may load the chunk if it isn't already!**
         *
         * @return The chunk this storage belongs to.
         */
        get() = worldStorage.world?.getChunkAt(coords.x, coords.y)

    protected val persistentContainer: PersistentDataContainer?
        /**
         * Gets the PersistentDataContainer of the Chunk this Storage belongs to.<br></br>
         * **This may load the chunk if it isn't already!**
         *
         * @return The chunk this storage belongs to.
         */
        get() = worldStorage.world?.getChunkAt(coords.x, coords.y)?.persistentDataContainer

    private val persistentBlocksContainer: PersistentDataContainer?
        get() = persistentContainer?.let { container ->
            val context = container.adapterContext
            if (!container.has(
                    BLOCKS_KEY,
                    PersistentDataType.TAG_CONTAINER
                )
            ) {
                container.set(
                    BLOCKS_KEY,
                    PersistentDataType.TAG_CONTAINER,
                    context.newPersistentDataContainer()
                )
            }
            container.get(
                BLOCKS_KEY,
                PersistentDataType.TAG_CONTAINER
            )
        }

    /**
     * Removes the stored block at this location and stops every active particle effect.<br></br>
     * *This converts the location to a Vector and uses [.removeBlock]*
     *
     * @param location The target location of the block
     * @return Optional of the previously stored data; otherwise empty Optional.
     * @see .removeBlock
     */
    fun removeBlock(location: Location): Optional<BlockStorage> {
        return removeBlock(location.toVector())
    }

    /**
     * Removes the BlockStorage at the specified position.
     *
     * @param pos The position vector of the BlockStorage
     * @return Optional of the previously stored data; otherwise empty Optional.
     * @see .removeBlock
     */
    fun removeBlock(pos: Vector): Optional<BlockStorage> {
        val previousStore = BLOCKS.remove(pos)
        updateBlock(pos)
        if (previousStore != null) {
            previousStore.onUnload()
            return Optional.of(previousStore)
        }
        return Optional.empty()
    }

    /**
     * Gets the BlockStorage if it exists; otherwise creates a new instance via [.createBlockStorage].<br></br>
     *
     * In case the BlockStorage doesn't exist yet, then the new BlockStorage instance is directly applied to the ChunkStorage.<br></br>
     *
     * If that is not required or desired, then [.getOrCreateBlockStorage] and [.setBlockStorageIfAbsent] provide the same functionality together.
     *
     * @param location The location of the Block.
     * @return The existing BlockStorage; otherwise a new BlockStorage Instance.
     */
    fun getOrCreateAndSetBlockStorage(location: Location): BlockStorage {
        val pos = location.toVector()
        val blockStorage = BLOCKS.computeIfAbsent(pos) { vector: Vector? -> createBlockStorage(location) }
        updateBlock(blockStorage.pos)
        return blockStorage
    }

    /**
     * Gets the BlockStorage if it exists; otherwise creates a new instance via [.createBlockStorage]
     *
     * @param location The location of the block.
     * @return The BlockStorage of the block if it exists; otherwise a new BlockStorage instance for the block.
     */
    fun getOrCreateBlockStorage(location: Location): BlockStorage {
        val pos = location.toVector()
        return BLOCKS.getOrDefault(pos, createBlockStorage(location))
    }

    /**
     * Creates a new BlockStorage for the specified location.<br></br>
     * The BlockStorage can then be applied to the block using [.setBlockStorageIfAbsent]
     *
     * @param location The location of the block.
     * @return The new instance of the BlockStorage.
     */
    fun createBlockStorage(location: Location): BlockStorage {
        val pos = location.toVector()
        val persistentBlockContainer = persistentContainer?.adapterContext?.newPersistentDataContainer() ?: throw RuntimeException("Failed to create PersistentDataContainer!")
        return BlockStorage(this, pos, persistentBlockContainer)
    }

    /**
     * Applies the specified BlockStorage to the ChunkStorage if it isn't occupied by another storage yet.
     *
     * @param blockStorage The BlockStorage to apply.
     */
    fun setBlockStorageIfAbsent(blockStorage: BlockStorage) {
        BLOCKS.putIfAbsent(blockStorage.pos, blockStorage)
        updateBlock(blockStorage.pos)
    }

    /**
     * Checks if there is an existing BlockStorage at the specified Location.
     *
     * @param location The location to check for a BlockStorage.
     * @return True if there exists a BlockStorage at the location; otherwise false.
     */
    fun isBlockStored(location: Location): Boolean {
        return BLOCKS.containsKey(location.toVector())
    }

    /**
     * Gets the stored block at the specified location.
     *
     * @param location The location of the block.
     * @return The stored block if stored; otherwise empty Optional.
     */
    fun getBlock(location: Location): Optional<BlockStorage> {
        return Optional.ofNullable(BLOCKS[location.toVector()])
    }

    val storedBlocks: Map<Vector, BlockStorage>
        /**
         * Gets the stored blocks in the chunk.
         *
         * @return The stored blocks in the chunk.
         */
        get() = BLOCKS.entries.stream().filter { entry: Map.Entry<Vector, BlockStorage> -> entry.value != null }
            .collect(
                Collectors.toMap<Map.Entry<Vector, BlockStorage>, Vector, BlockStorage>(
                    { it.key },
                    { it.value })
            )

    /**
     * Updates the specified block position in the PersistentStorageContainer.
     *
     * @param blockPos The block position to update.
     */
    fun updateBlock(blockPos: Vector) {
        persistentBlocksContainer?.let { blocks ->
            val value = BLOCKS[blockPos]
            val key = createKeyForBlock(blockPos)
            if (value != null && !value.isEmpty) { //Do not store empty storage in NBT, but keep them in cache.
                blocks.set(key, PersistentType(this, blockPos), value)
            } else {
                blocks.remove(key)
            }
            persistentContainer?.set(BLOCKS_KEY, PersistentDataType.TAG_CONTAINER, blocks)
        }
    }

    /**
     * Creates a new key for the specified block position.<br></br>
     * Format of key: "wolfyutils:&lt;+/-x&gt;_&lt;+/-y&gt;_&lt;+/-z&gt;"<br></br>
     * The key can have a maximum of 255 characters.<br></br>
     * "wolfyutils" + ":" + "_"*2 = 13 -> leaves space for 242 characters for x, y, and z including +/-.
     *
     * @param blockPos
     * @return
     */
    private fun createKeyForBlock(blockPos: Vector): NamespacedKey {
        return NamespacedKey(
            BLOCK_POS_NAMESPACE,
            BLOCK_POS_KEY.formatted(blockPos.blockX, blockPos.blockY, blockPos.blockZ)
        )
    }

    companion object {
        val BLOCKS_KEY: NamespacedKey = NamespacedKey("wolfyutils", "blocks")

        private const val BLOCK_POS_KEY = "%s_%s_%s" //x, y, z
        private const val BLOCK_POS_NAMESPACE = "wolfyutils" //-> "wolfyutils:x_y_z"

        /**
         * Creates a new ChunkStorage for the specified chunk coords and WorldStorage.
         *
         * @param worldStorage The parent WorldStorage.
         * @param coords       The chunk coords.
         * @return The newly created ChunkStorage instance.
         */
        fun create(worldStorage: WorldStorage, coords: Vec2i): ChunkStorage {
            return ChunkStorage(worldStorage, coords)
        }
    }
}
