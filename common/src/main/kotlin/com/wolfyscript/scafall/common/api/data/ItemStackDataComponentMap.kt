package com.wolfyscript.scafall.common.api.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.data.ItemStackDataComponentConverter
import com.wolfyscript.scafall.wrappers.world.items.ItemStack

class ItemStackDataComponentMap(private val holder: ItemStack) : DataComponentMap.Mutable<ItemStack> {

    override fun <T : Any> get(key: DataKey<T, in ItemStack>): T? {
        return ScafallProvider.get().registries.itemStackDataComponentConverterRegistry[key.key()]?.let {
            it as ItemStackDataComponentConverter<T> // We kinda make sure the type is correct by assigning the correct DataKeys
            it.reader.converter.invoke(holder)
        }
    }

    override fun <T : Any> set(key: DataKey<T, in ItemStack>, data: T) {
        ScafallProvider.get().registries.itemStackDataComponentConverterRegistry[key.key()]?.let {
            it as ItemStackDataComponentConverter<T> // We kinda make sure the type is correct by assigning the correct DataKeys
            it.writer.converter.invoke(holder, data)
        }
    }

    override fun remove(key: DataKey<*, in ItemStack>): Boolean {
        TODO("Not yet implemented")
    }

    override fun has(key: DataKey<*, in ItemStack>): Boolean {
        return get(key) != null
    }

    override fun keys(): Set<DataKey<*, in ItemStack>> {
        TODO("Not yet implemented")
    }

}