package com.wolfyscript.scafall.common.api.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.data.ItemDataComponentConverter
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot

class ItemStackSnapshotDataComponentMap(private val holder: ItemStackSnapshot) : DataComponentMap.Immutable<ItemStackSnapshot> {

    override fun <T : Any> get(key: DataKey<T, in ItemStackSnapshot>): T? {
        return ScafallProvider.get().registries.itemDataComponentConverterRegistry[key.key()]?.let {
            it as ItemDataComponentConverter<T> // We kinda make sure the type is correct by assigning the correct DataKeys
            it.reader.converter.invoke(holder).getOrThrow()
        }
    }

    override fun <T : Any> set(key: DataKey<T, in ItemStackSnapshot>, data: T) : ItemStackSnapshot {
        return ScafallProvider.get().registries.itemDataComponentConverterRegistry[key.key()]?.let {
            it as ItemDataComponentConverter<T> // We kinda make sure the type is correct by assigning the correct DataKeys

            val copy = holder.createStack()
            it.modifier.converter.invoke(copy, data)
            copy.snapshot()
        } ?: holder
    }

    override fun has(key: DataKey<*, in ItemStackSnapshot>): Boolean {
        return get(key) != null
    }

    override fun keys(): Set<DataKey<*, in ItemStackSnapshot>> {
        TODO("Not yet implemented")
    }

}