package com.wolfyscript.scafall.spigot.platform.persistent.world

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.math.Vec2i
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.util.Vector
import java.util.*

class WorldStorage(@JvmField val core: Scafall, private val worldUUID: UUID) {
    private val CHUNK_DATA: MutableMap<Vec2i, ChunkStorage> = HashMap()

    val world: World?
        get() = Bukkit.getWorld(worldUUID)

    fun getOrCreateChunkStorage(chunkCoords: Vec2i): ChunkStorage {
        return CHUNK_DATA.computeIfAbsent(chunkCoords) { ChunkStorage.create(this, it) }
    }

    fun getOrCreateChunkStorage(chunkX: Int, chunkZ: Int): ChunkStorage {
        return getOrCreateChunkStorage(Vec2i(chunkX, chunkZ))
    }

    fun getOrCreateChunkStorage(location: Location): ChunkStorage {
        return getOrCreateChunkStorage(Vec2i(location.blockX shr 4, location.blockZ shr 4))
    }

    private fun getOrCreateChunkStorage(pos: Vector): ChunkStorage {
        return getOrCreateChunkStorage(Vec2i(pos.blockX shr 4, pos.blockZ shr 4))
    }

    fun getOrCreateAndSetBlockStorage(location: Location): BlockStorage {
        return getOrCreateChunkStorage(location).getOrCreateAndSetBlockStorage(location)
    }

    /**
     * Gets the BlockStorage if it exists; otherwise creates a new instance via [.createBlockStorage]
     *
     * @param location The location of the block.
     * @return The BlockStorage of the block if it exists; otherwise a new BlockStorage instance for the block.
     */
    fun getOrCreateBlockStorage(location: Location): BlockStorage {
        return getOrCreateChunkStorage(location).getOrCreateBlockStorage(location)
    }

    /**
     * Creates a new BlockStorage for the specified location.<br></br>
     * The BlockStorage can then be applied to the block using [.setBlockStorageIfAbsent]
     *
     * @param location The location of the block.
     * @return The new instance of the BlockStorage.
     */
    fun createBlockStorage(location: Location): BlockStorage {
        return getOrCreateChunkStorage(location).createBlockStorage(location)
    }

    /**
     * Applies the specified BlockStorage to the ChunkStorage if it isn't occupied by another storage yet.
     *
     * @param blockStorage The BlockStorage to apply.
     */
    fun setBlockStorageIfAbsent(blockStorage: BlockStorage) {
        getOrCreateChunkStorage(blockStorage.pos).setBlockStorageIfAbsent(blockStorage)
    }

    fun unloadChunk(chunkStorage: ChunkStorage) {
        chunkStorage.chunk?.let { CHUNK_DATA.remove(Vec2i(it.x, it.z)) }
    }

    /**
     * Removes the stored block at this location and stops every active particle effect.
     *
     * @param location The target location of the block
     */
    fun removeBlock(location: Location): Optional<BlockStorage> {
        return getOrCreateChunkStorage(location).removeBlock(location)
    }

    fun isBlockStored(location: Location): Boolean {
        return getBlock(location).isPresent
    }

    fun getBlock(location: Location): Optional<BlockStorage> {
        return getOrCreateChunkStorage(location).getBlock(location)
    }

    protected val worldContainer: PersistentDataContainer?
        get() = world?.persistentDataContainer
}
