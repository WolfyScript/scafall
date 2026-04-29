package com.wolfyscript.scafall.items

import com.wolfyscript.scafall.wrappers.minecraft.unwrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack
import com.wolfyscript.scafall.wrappers.minecraft.wrap

/**
 * Constructs a new instance of a [ItemStackRef].
 */
internal class ItemStackRefImpl(
    override val amount: Int = 1,
    override val identifier: ItemStackIdentifier,
) : ItemStackRef {

    override fun matches(
        stack: ItemStackLike,
        matchTags: Boolean,
    ): Boolean {
        return  identifier.matches(stack, matchTags)
    }

    override fun create(): ScafallItemStack {
        val stack = identifier.create().unwrap()
        stack.count = amount
        return stack.wrap()
    }

    override fun toString(): String {
        return "ref(${amount} $identifier)"
    }

}