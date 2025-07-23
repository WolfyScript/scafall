package com.wolfyscript.scafall.common.api.items

import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.items.ItemStackRef
import com.wolfyscript.scafall.wrappers.utils.unwrap
import com.wolfyscript.scafall.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack

/**
 * Constructs a new instance of a [ItemStackRef].
 */
class ItemStackRefImpl(
    override val amount: Int,
    override val identifier: ItemStackIdentifier,
) : ItemStackRef {

    override fun matches(
        stack: ItemStack,
        matchTags: Boolean,
    ): Boolean {
        return  identifier.matches(stack, matchTags)
    }

    override fun create(): ItemStack {
        val stack = identifier.create().unwrap()
        stack.count = amount
        return stack.wrap()
    }

    override fun toString(): String {
        return "ref(${amount} $identifier)"
    }

}