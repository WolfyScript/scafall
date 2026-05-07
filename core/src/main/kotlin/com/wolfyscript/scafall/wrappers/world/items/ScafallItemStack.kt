package com.wolfyscript.scafall.wrappers.world.items

/**
 * A mutable version of the ItemStack.
 *
 * @see ItemStackSnapshot An immutable snapshot of the ItemStack
 */
interface ScafallItemStack : ItemStackLike {

    /**
     * Creates a snapshot of the whole ItemStack
     *
     * @return The snapshot ItemStack of this ItemStack.
     */
    fun snapshot(): ItemStackSnapshot

    companion object {

        /**
         * Wraps a Minecraft ItemStack into a ScafallItemStack.
         *
         * @param stack The Minecraft ItemStack to wrap.
         * @return A ScafallItemStack wrapping the given Minecraft ItemStack.
         */
        fun wrap(stack: net.minecraft.world.item.ItemStack): ScafallItemStack = ScafallItemStackImpl.wrap(stack)

    }
}

