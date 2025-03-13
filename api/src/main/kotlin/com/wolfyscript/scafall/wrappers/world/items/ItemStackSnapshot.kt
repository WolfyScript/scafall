package com.wolfyscript.scafall.wrappers.world.items

import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataHolder

/**
 * An immutable snapshot of an [ItemStack].
 * i.e. Changes made to this [DataComponentMap] or possibly other property will create a copy of the instance with the changes applied.
 *
 * @see ItemStack A mutable ItemStack
 */
interface ItemStackSnapshot : DataHolder.Immutable<ItemStackSnapshot>, ItemStackLike<ItemStackSnapshot, DataComponentMap.Immutable<ItemStackSnapshot>> {

     /**
      * Creates a mutable copy of this snapshot
      */
     fun createStack(): ItemStack

}