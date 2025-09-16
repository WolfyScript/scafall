package com.wolfyscript.scafall.wrappers.world.items

/**
 * An immutable snapshot of an [ScafallItemStack].
 *
 * @see ScafallItemStack A mutable ItemStack
 */
interface ItemStackSnapshot : ItemStackLike {

     /**
      * Creates a mutable copy of this snapshot
      */
     fun createStack(): ScafallItemStack

}