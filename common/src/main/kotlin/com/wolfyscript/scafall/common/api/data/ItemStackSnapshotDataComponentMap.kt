package com.wolfyscript.scafall.common.api.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.data.ItemStackDataComponentConverter
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot

class ItemStackSnapshotDataComponentMap(private val holder: ItemStackSnapshot) : DataComponentMap.Immutable<ItemStackSnapshot> {

    override fun <T : Any> get(key: DataKey<T, ItemStackSnapshot>): T? {
        return ScafallProvider.get().registries.itemStackDataComponentConverterRegistry[key.key()]?.let {
            it as ItemStackDataComponentConverter<T> // We kinda make sure the type is correct by assigning the correct DataKeys
            it.reader.converter.invoke(holder)
        }
    }

    override fun <T : Any> set(key: DataKey<T, ItemStackSnapshot>, data: T) : ItemStackSnapshot {
        return ScafallProvider.get().registries.itemStackDataComponentConverterRegistry[key.key()]?.let {
            it as ItemStackDataComponentConverter<T> // We kinda make sure the type is correct by assigning the correct DataKeys

            val copy = holder.createStack()
            it.writer.converter.invoke(copy, data)
            copy.snapshot()
        } ?: holder
    }

    override fun has(key: DataKey<*, ItemStackSnapshot>): Boolean {
        return get(key) != null
    }

    override fun keys(): Set<DataKey<*, ItemStackSnapshot>> {
        TODO("Not yet implemented")
    }

}