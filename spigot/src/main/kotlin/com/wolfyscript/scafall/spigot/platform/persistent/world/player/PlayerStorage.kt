package com.wolfyscript.scafall.spigot.platform.persistent.world.player

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.InjectableValues
import com.fasterxml.jackson.databind.ObjectMapper
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.config.jackson.JacksonUtil
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.spigot.platform.customPlayerData
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.util.*
import java.util.function.Function

/**
 * This class stores data for player entities.<br></br>
 * If the player is offline, then the stored data is inaccessible.<br></br>
 * <br></br>
 * [CustomPlayerData] stored using [.setData] or [.computeIfAbsent]
 * is directly cached and stored into the [PersistentDataContainer] of the player.<br></br>
 * When data is requested via [.getData] it first tries to look for the cached data, and if unavailable
 * it tries to load it from the [PersistentDataContainer]. Only if both fail to find the data it returns an empty value.
 */
class PlayerStorage(private val core: Scafall, private val playerUUID: UUID) {
    private val CACHED_DATA: MutableMap<Key, CustomPlayerData?> = HashMap()

    val player: Optional<Player>
        /**
         * Gets the player to which this storage belongs.
         *
         * @return The player linked to this storage; or empty Optional if not available or offline.
         */
        get() = Optional.ofNullable<Player>(Bukkit.getPlayer(playerUUID))

    val persistentDataContainer: Optional<PersistentDataContainer>
        /**
         * Gets the [PersistentDataContainer] from the owner (Player) that is used to store the data.
         *
         * @return The [PersistentDataContainer] of the owner; or empty Optional if owner is offline.
         */
        get() = player.map { obj: Player -> obj.persistentDataContainer }

    /**
     * Adds/Updates the specified custom data to/in this storage.<br></br>
     * The data is then cached and added to the persistent storage!<br></br>
     * Therefor this method should only be used a limited amount of times!
     *
     * @param data The data value to add/update
     * @return The previous cached value, if any; otherwise null
     */
    fun <T : CustomPlayerData?> setData(data: T): T? {
        val prev = CACHED_DATA.put(data!!.key(), data) as T?
        prev?.onUnload()
        persistentDataContainer.ifPresent { container: PersistentDataContainer ->
            val dataContainer: PersistentDataContainer =
                container.getOrDefault(
                    DATA_KEY,
                    PersistentDataType.TAG_CONTAINER,
                    container.getAdapterContext().newPersistentDataContainer()
                )
            val objectMapper = JacksonUtil.objectMapper // TODO: Global mapper
            try {
                data.onLoad()
                dataContainer.set(
                    data.key().bukkit(),
                    PersistentDataType.STRING,
                    objectMapper.writeValueAsString(data)
                )
            } catch (e: JsonProcessingException) {
                throw RuntimeException(e)
            }
            container.set(
                DATA_KEY,
                PersistentDataType.TAG_CONTAINER,
                dataContainer
            )
        }
        return prev
    }

    /**
     * Gets the data that is saved under the specified type, or if not available gets and sets the default data.
     *
     * @param dataType     The type of the data.
     * @param defaultValue Function that creates the default value.
     * @param <T>          The type of the data.
     * @return The existing data; or the newly set default value.
    </T> */
    fun <T : CustomPlayerData> computeIfAbsent(dataType: Class<T>, defaultValue: Function<Class<T>?, T>): T? {
        return getData(dataType)?.let {
            val data = defaultValue.apply(dataType)
            setData(data)
            data
        }
    }

    /**
     * Gets the data that is saved under the specified type.<br></br>
     * In case the value is not yet loaded into the cache it deserializes it from the persistent data container.<br></br>
     * If the persistent data container is not available, which is the case when the player is offline, it returns an empty Optional.<br></br>
     *
     * @param dataType The type of the data. Must be registered in [BukkitRegistries.getCustomPlayerData]!
     * @param <T>      The type of the data.
     * @return The data of the specified type; or empty Optional when not available.
    </T> */
    fun <T : CustomPlayerData> getData(dataType: Class<out T>): T? {
        val dataID: Key = core.registries.customPlayerData.getKey(dataType) ?: return null
        // Might be null if the type wasn't registered. Check it just in case.
        var dataResult = dataType.cast(CACHED_DATA[dataID])
        if (dataResult == null) {
            // If there isn't any cached data yet
            val data =
                persistentDataContainer.map<T?> { container: PersistentDataContainer ->
                    val dataContainer: PersistentDataContainer =
                        container.getOrDefault(DATA_KEY, PersistentDataType.TAG_CONTAINER, container.getAdapterContext().newPersistentDataContainer())
                    val objectMapper: ObjectMapper = JacksonUtil.objectMapper
                    val key: NamespacedKey = dataID.bukkit()
                    if (dataContainer.has<String, String>(key, PersistentDataType.STRING)) {
                        val jsonData: String? = dataContainer.get(key, PersistentDataType.STRING)
                        if (jsonData != null && jsonData != "null" && jsonData.isNotBlank()) {
                            try {
                                return@map objectMapper.reader(
                                    InjectableValues.Std()
                                        .addValue(Scafall::class.java, core)
                                        .addValue(UUID::class.java, playerUUID)
                                ).forType(CustomPlayerData::class.java).readValue(jsonData)
                            } catch (e: JsonProcessingException) {
//                                core.getLogger()
//                                    .warning("Unable to load custom data from '$jsonData'! Removing it now to prevent further issues!")
                                // Directly modify the container instead of calling removeData() as the unload method will never be called anyway.
                                dataContainer.remove(key)
                                container.set(
                                    DATA_KEY,
                                    PersistentDataType.TAG_CONTAINER,
                                    dataContainer
                                )
                            }
                        }
                    }
                    null
                }
            dataResult = data.orElse(null)
            CACHED_DATA[dataID] = dataResult
        }
        return dataResult
    }

    /**
     * Removes the data saved under the specified id.
     *
     * @param dataType The type of the data. Must be registered in [BukkitRegistries.getCustomPlayerData]!
     * @param <T>      The type of the data.
     * @return The removed data value; or null if nothing was removed.
    </T> */
    fun <T : CustomPlayerData> removeData(dataType: Class<out T>): T? {
        val dataID: Key = core.registries.customPlayerData.getKey(dataType) ?: return null
        val prev = dataType.cast(CACHED_DATA.remove(dataID))
        prev?.onUnload()
        persistentDataContainer.ifPresent { container ->
            val dataContainer: PersistentDataContainer =
                container.getOrDefault(
                    DATA_KEY,
                    PersistentDataType.TAG_CONTAINER,
                    container.getAdapterContext().newPersistentDataContainer()
                )
            dataContainer.remove(dataID.bukkit())
            container.set(
                DATA_KEY,
                PersistentDataType.TAG_CONTAINER,
                dataContainer
            )
        }
        return prev
    }

    /**
     * Removes the data saved under the specified id.
     *
     * @param dataID The id of the data.
     * @return The removed data value; or null if nothing was removed.
     */
    fun removeData(dataID: Key): CustomPlayerData? {
        return core.registries.customPlayerData[dataID]?.let { removeData(it) }
    }

    /**
     * Updates all the currently cached values in the persistent data container
     * and clears the cache afterwards.
     */
    fun updateAndClearCache() {
        update()
        CACHED_DATA.clear()
    }

    /**
     * Updates all the currently cached values in the persistent data container.
     */
    fun update() {
        if (CACHED_DATA.isEmpty()) return
        persistentDataContainer.ifPresent { container: PersistentDataContainer ->
            val dataContainer: PersistentDataContainer =
                container.getOrDefault(
                    DATA_KEY,
                    PersistentDataType.TAG_CONTAINER,
                    container.getAdapterContext().newPersistentDataContainer()
                )
            val objectMapper = JacksonUtil.objectMapper
            for ((key, value) in CACHED_DATA) {
                try {
                    dataContainer.set(key.bukkit(), PersistentDataType.STRING, objectMapper.writeValueAsString(value))
                } catch (e: JsonProcessingException) {
                    throw RuntimeException(e)
                }
            }
            container.set(DATA_KEY, PersistentDataType.TAG_CONTAINER, dataContainer)
        }
    }

    companion object {
        private val DATA_KEY: NamespacedKey = NamespacedKey("wolfyutils", "data")
    }
}
