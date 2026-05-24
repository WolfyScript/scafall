package com.wolfyscript.scafall.sponge.api.wrappers

import com.wolfyscript.scafall.sponge.api.wrappers.world.entity.SpongeScafallPlayer
import com.wolfyscript.scafall.wrappers.ScafallPlayer
import com.wolfyscript.scafall.wrappers.snapshot
import com.wolfyscript.scafall.wrappers.unwrap
import com.wolfyscript.scafall.wrappers.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.common.item.util.ItemStackUtil

fun ItemStack.wrap() : com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack {
    return ItemStackUtil.toNative(this).wrap() // Sponge uses mixins, so the MC ItemStack implements ItemStack interface
}

fun Player.wrap(): SpongeScafallPlayer {
    return SpongeScafallPlayer(this)
}

fun ItemStackSnapshot.wrap() : com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot {
    return ItemStackUtil.fromSnapshotToNative(this).snapshot()
}

fun <T: ItemStackLike> T.unwrap(): org.spongepowered.api.item.inventory.ItemStackLike {
    TODO("Not yet implemented")
}

fun com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack.unwrap(): ItemStack {
    return ItemStackUtil.fromNative(this.unwrap())
}

fun ScafallPlayer.unwrap(): Player {
    return (this as SpongeScafallPlayer).ref.get() ?: throw Exception("Player is null")
}
