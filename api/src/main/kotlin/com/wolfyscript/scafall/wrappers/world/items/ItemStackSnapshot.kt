package com.wolfyscript.scafall.wrappers.world.items

interface ItemStackSnapshot : ItemStackLike {

     fun createStack(): ItemStack

}