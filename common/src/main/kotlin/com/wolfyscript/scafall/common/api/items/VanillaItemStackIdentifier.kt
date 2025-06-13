package com.wolfyscript.scafall.common.api.items

import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.wrappers.utils.unwrap
import com.wolfyscript.scafall.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack

class VanillaItemStackIdentifier(val stack: ItemStack) : ItemStackIdentifier {

    override val parser: ItemStackIdentifier.Parser<*>
        get() = TODO("Not yet implemented")

    override fun matches(
        stack: ItemStack,
        matchTags: Boolean,
    ): Boolean {
        val other = stack.unwrap()
        val thisStack = this.stack.unwrap()
        if (other.count != thisStack.count) {
            return false
        }
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
        return "VanillaItemStackIdentifier(stack=$stack)"
    }

    class Parser : ItemStackIdentifier.Parser<VanillaItemStackIdentifier> {

        override val priority: Int = 0

        override fun from(stack: ItemStack): VanillaItemStackIdentifier? {
            return VanillaItemStackIdentifier(stack)
        }

    }

}
