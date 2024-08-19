package com.wolfyscript.scafall.spigot.platform.persistent.world.player

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import com.wolfyscript.scafall.config.jackson.KeyedTypeIdResolver
import com.wolfyscript.scafall.config.jackson.KeyedTypeResolver
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Keyed
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

/**
 * Custom Data that can be applied to Players and persists across server restarts.<br></br>
 * <br></br>
 * How to create custom data:<br></br>
 * - Extend this class<br></br>
 *
 */
@JsonTypeResolver(KeyedTypeResolver::class)
@JsonTypeIdResolver(
    KeyedTypeIdResolver::class
)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "id")
@JsonPropertyOrder(value = ["id"])
abstract class CustomPlayerData
/**
 * The default constructor that must get the id of the custom data type.
 * @param id The id of the custom data type.
 */ protected constructor(@field:JsonProperty("id") private val id: Key) : Keyed {
    /**
     * Called when the CustomPlayerData is initialising its data.
     * This happens right before it is added to the player, so the PlayerStorage does not yet contain it!
     */
    abstract fun onLoad()

    /**
     * Called when the CustomPlayerData is removed from the Player.
     * This happens right before it is removed from the player, so the PlayerStorage still contains it!
     */
    abstract fun onUnload()

    /**
     * Copies the custom data.
     * @return A deep-copy of this custom data.
     */
    abstract fun copy(): CustomPlayerData?

    /**
     * Convenience method to get the player by uuid.
     *
     * @param uuid The uuid of the player.
     * @return The online player with that uuid; or empty optional if not online.
     */
    protected fun getPlayer(uuid: UUID): Optional<Player> {
        return Optional.ofNullable(Bukkit.getPlayer(uuid))
    }

    @JsonIgnore
    override fun key(): Key {
        return id
    }
}
