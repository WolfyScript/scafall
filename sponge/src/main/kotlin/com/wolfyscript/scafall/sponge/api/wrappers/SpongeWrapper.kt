package com.wolfyscript.scafall.sponge.api.wrappers

import com.wolfyscript.scafall.sponge.api.wrappers.world.items.ItemStackWrapper
import org.spongepowered.api.item.inventory.ItemStack

fun ItemStack.wrap() : com.wolfyscript.scafall.wrappers.world.items.ItemStack {
    return ItemStackWrapper(this)
}

fun com.wolfyscript.scafall.wrappers.world.items.ItemStack.unwrap(): ItemStack {
    return (this as ItemStackWrapper).ref
}