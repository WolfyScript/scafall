package com.wolfyscript.scafall.common.api.data

import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataHolder
import com.wolfyscript.scafall.data.DataKey

class CommonDataComponentMap<C: DataHolder<C>>(private val holder: C) : DataComponentMap<C> {

    override fun remove(key: DataKey<*, C>): Boolean {
        return true
    }

    override fun <T: Any> get(key: DataKey<T, C>): T? {
        return key.readFrom(holder)
    }

    override fun has(key: DataKey<*, C>): Boolean {
        return key.readFrom(holder) != null
    }

    override fun <T: Any> set(key: DataKey<T, C>, data: T) {
        key.writeTo(data, holder)
    }

    override fun keys(): Set<DataKey<*, C>> {
        return emptySet()
    }

}