package com.wolfyscript.scafall.wrappers.world.items

/**
 * An immutable snapshot of an [ItemStack].
 *
 * @see ItemStack A mutable ItemStack
 */
interface ItemStackSnapshot : ItemStackLike {

     /**
      * Creates a mutable copy of this snapshot
      */
     fun createStack(): ItemStack

}