package com.wolfyscript.scafall.sponge.api.wrappers

import com.wolfyscript.scafall.sponge.api.wrappers.world.entity.SpongePlayer
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.SpongeItemStack
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.SpongeItemStackSnapshot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot

fun ItemStack.wrap() : com.wolfyscript.scafall.wrappers.world.items.ItemStack {
    return SpongeItemStack(this)
}

fun Player.wrap(): SpongePlayer {
    return SpongePlayer(this)
}

fun ItemStackSnapshot.wrap() : com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot {
    return SpongeItemStackSnapshot(this)
}

fun <T: ItemStackLike<*,*>> T.unwrap(): org.spongepowered.api.item.inventory.ItemStackLike {
    return when (this) {
        is SpongeItemStack -> { ref }
        is SpongeItemStackSnapshot -> { ref }
        else -> throw Exception("Cannot unwrap ItemStackLike of type ${this.javaClass}")
    }
}

fun com.wolfyscript.scafall.wrappers.world.items.ItemStack.unwrap(): ItemStack {
    return (this as SpongeItemStack).ref
}

fun com.wolfyscript.scafall.wrappers.world.entity.Player.unwrap(): Player {
    return (this as SpongePlayer).ref
}
