package com.wolfyscript.scafall.items

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike

/**
 * Reference to an item stack from an external source.
 * The [identifier] specifies where the stack is coming from and how to resolve it.
 */
interface ItemStackRef {

    companion object {

        fun create(stack: ItemStack, count: Int = stack.amount): ItemStackRef {
            return ScafallProvider.get().factories.itemsFactory.createVanillaStackRef(stack, count)
        }

        fun parse(stack: ItemStack, count: Int = stack.amount): ItemStackRef? {
            return ScafallProvider.get().factories.itemsFactory.parseStackRef(stack, count)
        }

    }

    /**
     * The amount of the stack that will be created by [create].
     */
    val amount: Int

    /**
     * The identifier, specifying where the stack is coming from and how to resolve it.
     */
    val identifier: ItemStackIdentifier

    /**
     * Checks if the given [stack] matches this reference.
     */
    fun matches(stack: ItemStackLike<*,*>, matchTags: Boolean): Boolean

    /**
     * Creates the ItemStack from this reference by looking up the [identifier] and constructing it.
     */
    fun create(): ItemStack

}