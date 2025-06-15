package com.wolfyscript.scafall.common.api.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.data.ItemDataComponentConverter
import com.wolfyscript.scafall.wrappers.world.items.ItemStack

class ItemStackDataComponentMap(private val holder: ItemStack) : DataComponentMap.Mutable<ItemStack> {

    override fun <T : Any> get(key: DataKey<T, in ItemStack>): T? {
        return ScafallProvider.get().registries.itemDataComponentConverterRegistry[key.key]?.let {
            it as ItemDataComponentConverter<T> // We kinda make sure the type is correct by assigning the correct DataKeys
            it.reader.converter.invoke(holder)
                .getOrThrow() // Throw exception here for now. Could/should we propagate it further?
        }
    }

    override fun <T : Any> set(key: DataKey<T, in ItemStack>, data: T) {
        ScafallProvider.get().registries.itemDataComponentConverterRegistry[key.key]?.let {
            it as ItemDataComponentConverter<T> // We kinda make sure the type is correct by assigning the correct DataKeys
            it.modifier.converter.invoke(holder, data)
                .getOrThrow() // Throw exception here for now. Could/should we propagate it further?
        }
    }

    override fun remove(key: DataKey<*, in ItemStack>): Boolean {
        return ScafallProvider.get().registries.itemDataComponentConverterRegistry[key.key]?.modifier?.remover?.invoke(
            holder
        )?.getOrThrow()?.second == true
    }

    override fun has(key: DataKey<*, in ItemStack>): Boolean {
        return get(key) != null
    }

    override fun keys(): Set<DataKey<*, in ItemStack>> {
        TODO("Not yet implemented")
    }

}