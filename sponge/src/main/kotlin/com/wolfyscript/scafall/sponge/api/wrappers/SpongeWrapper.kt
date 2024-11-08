package com.wolfyscript.scafall.sponge.api.wrappers

import com.wolfyscript.scafall.sponge.api.wrappers.world.items.ItemStackWrapper
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.SpongeItemStackSnapshot
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot

fun ItemStack.wrap() : com.wolfyscript.scafall.wrappers.world.items.ItemStack {
    return ItemStackWrapper(this)
}

fun ItemStackSnapshot.wrap() : com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot {
    return SpongeItemStackSnapshot(this)
}

fun com.wolfyscript.scafall.wrappers.world.items.ItemStack.unwrap(): ItemStack {
    return (this as ItemStackWrapper).ref
}