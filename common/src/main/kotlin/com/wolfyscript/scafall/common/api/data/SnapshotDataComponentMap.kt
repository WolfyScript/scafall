package com.wolfyscript.scafall.common.api.data

import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataHolder
import com.wolfyscript.scafall.data.DataKey
import kotlin.reflect.cast

class SnapshotDataComponentMap<C: DataHolder<C>> : DataComponentMap<C> {

    private val componentMap: MutableMap<DataKey<*, C>, Any> = mutableMapOf()

    override fun remove(key: DataKey<*, C>): Boolean {
        return componentMap.remove(key) != null
    }

    override fun <T: Any> get(key: DataKey<T, C>): T? {
        return componentMap[key]?.let { key.type.cast(it) }
    }

    override fun has(key: DataKey<*, C>): Boolean {
        return componentMap.containsKey(key)
    }

    override fun <T: Any> set(key: DataKey<T, C>, data: T) {
        componentMap[key] = data
    }

    override fun keys(): Set<DataKey<*, C>> {
        return componentMap.keys
    }

}