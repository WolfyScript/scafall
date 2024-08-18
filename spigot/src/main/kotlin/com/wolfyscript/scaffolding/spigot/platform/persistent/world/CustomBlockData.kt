package com.wolfyscript.scaffolding.spigot.platform.persistent.world

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeIdResolver
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeResolver
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Keyed

/**
 * This data is used to store persistent data on Blocks.<br></br>
 * The data is saved directly inside the Chunks' [org.bukkit.persistence.PersistentDataContainer], so it persists across server restarts.<br></br>
 * In order to save it the [CustomBlockData] is serialized into a JsonString using Jackson.<br></br>
 * The String is then saved into the [org.bukkit.persistence.PersistentDataContainer] with the id ([org.bukkit.NamespacedKey]) as the key.<br></br>
 * <br></br>
 * **To make use of the implementation it must be registered via [BukkitRegistries.getCustomBlockData]!**
 * <br></br>
 * On Deserialization the key is used to find the registered data type (See [BukkitRegistries.getCustomBlockData])<br></br>
 * The String content is then deserialized to that type using Jackson.<br></br>
 * There are injectable values that can be used in the constructor to get access to the Core, ChunkStorage, Position, etc.<br></br>
 *
 *  * [com.wolfyscript.utilities.bukkit.WolfyCoreCommon]
 *  * [ChunkStorage]
 *  * [org.bukkit.util.Vector]
 *
 * That can be injected using the [com.fasterxml.jackson.annotation.JacksonInject] annotation.<br></br>
 * <br></br>
 * One of the default data, that stores the CustomItems on blocks is [CustomItemBlockData]
 */
@JsonTypeResolver(KeyedTypeResolver::class)
@JsonTypeIdResolver(
    KeyedTypeIdResolver::class
)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "id")
@JsonPropertyOrder(value = ["id"])
abstract class CustomBlockData protected constructor(@field:JsonProperty("id") private val id: Key) : Keyed {
    /**
     * Called when the BlockStorage is initialising its data.
     * Usually right after the data was constructed.
     */
    abstract fun onLoad()

    /**
     * Called when the BlockStorage is removed from the ChunkStorage or the Chunk is unloaded.
     */
    abstract fun onUnload()

    abstract fun copy(): CustomBlockData

    /**
     * Copies this data to the other BlockStorage.
     *
     * @param storage The other BlockStorage to copy the data to.
     * @return The data that was copied to the other BlockStorage.
     */
    abstract fun copyTo(storage: BlockStorage): CustomBlockData

    @JsonIgnore
    override fun key(): Key {
        return id
    }
}
