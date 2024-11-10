package com.wolfyscript.scafall.wrappers.world.items

import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataHolder

interface ItemStackSnapshot : DataHolder.Immutable<ItemStackSnapshot>, ItemStackLike<ItemStackSnapshot, DataComponentMap.Immutable<ItemStackSnapshot>> {

     fun createStack(): ItemStack

}