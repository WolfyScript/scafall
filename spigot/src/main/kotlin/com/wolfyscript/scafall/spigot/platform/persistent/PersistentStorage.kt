package com.wolfyscript.scafall.spigot.platform.persistent

import com.google.common.base.Preconditions
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.spigot.platform.persistent.world.WorldStorage
import com.wolfyscript.scafall.spigot.platform.persistent.world.player.PlayerStorage
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.*

/**
 * The PersistentStorage API allows plugins to store custom complex data into block/chunks and players.<br></br>
 * It uses the [org.bukkit.persistence.PersistentDataHolder] and [org.bukkit.persistence.PersistentDataContainer] API to store the data.<br></br>
 * <br></br>
 * This class keeps track of the cached storage instances of worlds and players, and possibly more.
 *
 */
class PersistentStorage(val core: Scafall) {

    private val WORLD_STORAGE: MutableMap<UUID, WorldStorage> = HashMap()
    private val PLAYER_STORAGE: MutableMap<UUID, PlayerStorage> = HashMap()

    /**
     * Gets the already existing storage instance of that world or creates a new one.
     *
     * @param world The world to get/create the storage for.
     * @return The world storage instance of the specified world.
     */
    fun getOrCreateWorldStorage(world: World): WorldStorage {
        Preconditions.checkNotNull(world, "The world cannot be null!")
        return WORLD_STORAGE.computeIfAbsent(world.uid) { uuid -> WorldStorage(core, uuid) }
    }

    /**
     * Gets the existing storage instance of the given player, or creates a new one when it doesn't exist.
     *
     * @param player The player to get the data for.
     * @return The player storage instance of the specified player.
     */
    fun getOrCreatePlayerStorage(player: Player): PlayerStorage {
        Preconditions.checkNotNull(player, "The player cannot be null!")
        return PLAYER_STORAGE.computeIfAbsent(player.uniqueId) { uuid: UUID? ->
            PlayerStorage(
                core,
                uuid!!
            )
        }
    }
}
