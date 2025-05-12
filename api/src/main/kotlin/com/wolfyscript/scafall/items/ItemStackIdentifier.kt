package com.wolfyscript.scafall.items

import com.wolfyscript.scafall.wrappers.world.items.ItemStack

interface ItemStackIdentifier {

    val parser: Parser<*>

    fun matches(stack: ItemStack, matchTags: Boolean): Boolean

    fun create(): ItemStack

    interface Parser<T : ItemStackIdentifier> {

        val priority: Int

        fun from(stack: ItemStack): T?

    }

}