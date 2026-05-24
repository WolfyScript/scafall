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
     fun create(): ScafallItemStack

     companion object {

          /**
           * Wraps a [net.minecraft.world.item.ItemStack] into an immutable [ItemStackSnapshot].
           *
           * @param stack The ItemStack to wrap
           * @return An immutable ItemStackSnapshot
           */
          fun wrap(stack: net.minecraft.world.item.ItemStack): ItemStackSnapshot {
               return ItemStackSnapshotImpl.wrap(stack)
          }

     }

}