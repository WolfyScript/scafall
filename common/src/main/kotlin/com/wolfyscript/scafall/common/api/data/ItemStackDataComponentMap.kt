package com.wolfyscript.scafall.common.api.data

import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.wrappers.world.items.ItemStack

class ItemStackDataComponentMap(private val holder: ItemStack) : DataComponentMap.Mutable<ItemStack> {

    override fun <T : Any> get(key: DataKey<T, in ItemStack>): T? {
        return null
    }

    override fun <T : Any> set(key: DataKey<T, in ItemStack>, data: T) {
    }

    override fun remove(key: DataKey<*, in ItemStack>): Boolean {
        return false
    }

    override fun has(key: DataKey<*, in ItemStack>): Boolean {
        return get(key) != null
    }

    override fun keys(): Set<DataKey<*, in ItemStack>> {
        TODO("Not yet implemented")
    }

}