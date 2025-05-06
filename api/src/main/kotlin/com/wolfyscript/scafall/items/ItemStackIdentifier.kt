package com.wolfyscript.scafall.items

import com.wolfyscript.scafall.wrappers.world.items.ItemStack

interface ItemStackIdentifier {

    val parser: Parser<*>

    interface Parser<T : ItemStackIdentifier> {

        val priority: Int

        fun from(stack: ItemStack): T?

    }

}