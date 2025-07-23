package com.wolfyscript.scafall.items

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.wrappers.world.items.ItemStack

interface ItemStackRef {

    companion object {

        fun create(stack: ItemStack, count: Int = stack.amount): ItemStackRef {
            return ScafallProvider.get().factories.itemsFactory.createVanillaStackRef(stack, count)
        }

        fun parse(stack: ItemStack, count: Int = stack.amount): ItemStackRef? {
            return ScafallProvider.get().factories.itemsFactory.parseStackRef(stack, count)
        }

    }

    val amount: Int

    /**
     * The identifier used to construct this ref
     */
    val identifier: ItemStackIdentifier

    fun matches(stack: ItemStack, matchTags: Boolean): Boolean

    fun create(): ItemStack

}