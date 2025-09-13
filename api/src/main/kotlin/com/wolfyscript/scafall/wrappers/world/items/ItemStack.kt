package com.wolfyscript.scafall.wrappers.world.items

/**
 * A mutable version of the ItemStack.
 *
 * @see ItemStackSnapshot An immutable snapshot of the ItemStack
 */
interface ItemStack : ItemStackLike {

    /**
     * Creates a snapshot of the whole ItemStack
     *
     * @return The snapshot ItemStack of this ItemStack.
     */
    fun snapshot(): ItemStackSnapshot

}
