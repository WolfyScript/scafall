package com.wolfyscript.scafall.common.api.items

import com.wolfyscript.scafall.items.VanillaItemStackIdentifier
import com.wolfyscript.scafall.wrappers.unwrap
import com.wolfyscript.scafall.wrappers.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike

class VanillaItemStackIdentifierImpl(override val stack: ItemStack) : VanillaItemStackIdentifier {

    override fun matches(
        stack: ItemStackLike,
        matchTags: Boolean,
    ): Boolean {
        val other = stack.unwrap()
        val thisStack = this.stack.unwrap()
        if (thisStack == other) {
            return true // Same instance of stacks, so they must be equal!
        }
        if (matchTags) {
            return net.minecraft.world.item.ItemStack.isSameItemSameComponents(thisStack, other)
        }
        return net.minecraft.world.item.ItemStack.isSameItem(thisStack, other)
    }

    override fun create(): ItemStack {
        return stack.unwrap().copy().wrap()
    }

    override fun toString(): String {
        return "vanilla($stack)"
    }

    class Parser : VanillaItemStackIdentifier.Parser {

        override val priority: Int = 0

        override fun from(stack: ItemStackLike): VanillaItemStackIdentifierImpl? {
            return VanillaItemStackIdentifierImpl(stack.unwrap().wrap())
        }

    }

}
