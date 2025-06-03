package com.wolfyscript.scafall.sponge.api.wrappers

import com.wolfyscript.scafall.sponge.api.wrappers.world.entity.SpongePlayer
import com.wolfyscript.scafall.wrappers.utils.snapshot
import com.wolfyscript.scafall.wrappers.utils.unwrap
import com.wolfyscript.scafall.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.common.item.util.ItemStackUtil

fun ItemStack.wrap() : com.wolfyscript.scafall.wrappers.world.items.ItemStack {
    return ItemStackUtil.toNative(this).wrap() // Sponge uses mixins, so the MC ItemStack implements ItemStack interface
}

fun Player.wrap(): SpongePlayer {
    return SpongePlayer(this)
}

fun ItemStackSnapshot.wrap() : com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot {
    return ItemStackUtil.fromSnapshotToNative(this).snapshot()
}

fun <T: ItemStackLike<*,*>> T.unwrap(): org.spongepowered.api.item.inventory.ItemStackLike {
    TODO("Not yet implemented")
}

fun com.wolfyscript.scafall.wrappers.world.items.ItemStack.unwrap(): ItemStack {
    return ItemStackUtil.fromNative(this.unwrap())
}

fun com.wolfyscript.scafall.wrappers.world.entity.Player.unwrap(): Player {
    return (this as SpongePlayer).ref.get() ?: throw Exception("Player is null")
}
