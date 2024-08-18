package com.wolfyscript.scaffolding.spigot.platform.persistent.world

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.InjectableValues
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.config.jackson.JacksonUtil
import com.wolfyscript.scaffolding.config.jackson.JacksonUtil.objectMapper
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.spigot.api.identifiers.api
import com.wolfyscript.scaffolding.spigot.api.identifiers.bukkit
import com.wolfyscript.scaffolding.spigot.platform.customBlockData
import com.wolfyscript.scaffolding.spigot.platform.persistent.world.ChunkStorage
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector
import java.util.*
import java.util.function.Consumer

class BlockStorage(
    val chunkStorage: ChunkStorage,
    val pos: Vector,
    private val persistentContainer: PersistentDataContainer
) {
    private val core = chunkStorage.core

    private val data: MutableMap<Key, CustomBlockData> = HashMap()

    val isEmpty: Boolean
        get() = data.isEmpty()

    fun remove() {
        chunkStorage.removeBlock(pos)
    }

    fun onUnload() {
        data.values.forEach(Consumer { obj: CustomBlockData -> obj.onUnload() })
    }

    fun onLoad() {
        data.values.forEach(Consumer { obj: CustomBlockData -> obj.onLoad() })
    }

    fun addOrSetData(blockData: CustomBlockData?) {
        if (blockData != null) {
            val dataTypeRegistry = core.registries.customBlockData
            if (dataTypeRegistry.keySet().contains(blockData.key())) {
                data[blockData.key()] = blockData
            }
        }
    }

    fun <D : CustomBlockData> getData(key: Key, dataType: Class<D>): Optional<D> {
        val customData = data[key]
        if (customData != null) {
            if (dataType.isInstance(customData)) {
                return Optional.of(dataType.cast(customData))
            }
        }
        return Optional.empty()
    }

    val dataValues: Collection<CustomBlockData>
        get() = data.values

    private val persistentData: PersistentDataContainer?
        get() {
            if (!persistentContainer.has(
                    DATA_KEY,
                    PersistentDataType.TAG_CONTAINER
                )
            ) {
                persistentContainer.set(
                    DATA_KEY,
                    PersistentDataType.TAG_CONTAINER,
                    persistentContainer.adapterContext.newPersistentDataContainer()
                )
            }
            return persistentContainer.get(
                DATA_KEY,
                PersistentDataType.TAG_CONTAINER
            )
        }

    private fun saveToPersistent() {
        val objectMapper = JacksonUtil.objectMapper
        val dataPersistent = persistentData
        for ((key, value) in data) {
            try {
                dataPersistent?.set(key.bukkit(), PersistentDataType.STRING, objectMapper.writeValueAsString(value))
            } catch (e: JsonProcessingException) {
                throw RuntimeException(e)
            }
        }
        persistentContainer.set(
            DATA_KEY, PersistentDataType.TAG_CONTAINER,
            dataPersistent!!
        )
    }

    private fun loadFromPersistent(chunkStorage: ChunkStorage) {
        val dataTypeRegistry = core.registries.customBlockData
        val objectMapper = objectMapper
        val dataPersistent = persistentData
        for (key in dataPersistent!!.keys) {
            val wuKey: Key = key.api()
            val customDataString = dataPersistent.get(key, PersistentDataType.STRING)
            var blockData: CustomBlockData? = null
            try {
                blockData = objectMapper.reader(
                    InjectableValues.Std()
                        .addValue(Scaffolding::class.java, core)
                        .addValue(ChunkStorage::class.java, chunkStorage)
                        .addValue(Vector::class.java, pos)
                ).forType(CustomBlockData::class.java).readValue(customDataString)
            } catch (e: JsonProcessingException) {
//                core.getLogger().severe("Failed to load custom block data \"" + key + "\" at pos " + pos);
                e.printStackTrace()
            }
            if (blockData != null) {
                data[wuKey] = blockData
            }
        }
    }

    fun copyToOtherBlockStorage(storage: BlockStorage) {
        data.values.forEach(Consumer { customBlockData: CustomBlockData ->
            val copy = customBlockData.copyTo(storage)
            storage.addOrSetData(copy)
        })
        storage.onLoad()
    }

    class PersistentType(private val chunkStorage: ChunkStorage, private val pos: Vector) :
        PersistentDataType<PersistentDataContainer, BlockStorage> {
        override fun getPrimitiveType(): Class<PersistentDataContainer> {
            return PersistentDataContainer::class.java
        }

        override fun getComplexType(): Class<BlockStorage> {
            return BlockStorage::class.java
        }

        override fun toPrimitive(
            complex: BlockStorage,
            context: PersistentDataAdapterContext
        ): PersistentDataContainer {
            complex.saveToPersistent()
            return complex.persistentContainer
        }

        override fun fromPrimitive(data: PersistentDataContainer, context: PersistentDataAdapterContext): BlockStorage {
            val blockStorage = BlockStorage(chunkStorage, pos.clone(), data)
            blockStorage.loadFromPersistent(chunkStorage)
            return blockStorage
        }
    }


    companion object {
        private val DATA_KEY = NamespacedKey("wolfyutils", "data")
    }
}
