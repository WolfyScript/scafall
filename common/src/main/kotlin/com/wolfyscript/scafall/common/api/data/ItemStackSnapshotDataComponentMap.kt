package com.wolfyscript.scafall.common.api.data

import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot

class ItemStackSnapshotDataComponentMap(private val holder: ItemStackSnapshot) : DataComponentMap.Immutable<ItemStackSnapshot> {

    override fun <T : Any> get(key: DataKey<T, in ItemStackSnapshot>): T? {
        return null
    }

    override fun <T : Any> set(key: DataKey<T, in ItemStackSnapshot>, data: T) : ItemStackSnapshot {
        return holder
    }

    override fun has(key: DataKey<*, in ItemStackSnapshot>): Boolean {
        return get(key) != null
    }

    override fun keys(): Set<DataKey<*, in ItemStackSnapshot>> {
        TODO("Not yet implemented")
    }

}