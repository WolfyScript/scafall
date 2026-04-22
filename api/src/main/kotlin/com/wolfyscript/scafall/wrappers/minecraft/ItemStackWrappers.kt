package com.wolfyscript.scafall.wrappers.minecraft

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLikeCommon
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshotCommon
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStackCommon


/**
 * Wraps this Minecraft ItemStack in a scafall [ScafallItemStack].
 *
 * **Any changes** made to the wrapped stack, once unwrapped, **are reflected on this original stack**.
 */
fun net.minecraft.world.item.ItemStack.wrap(): ScafallItemStack = ScafallItemStackCommon.fromVanilla(this)

/**
 * Wraps a snapshot of this ItemStack in a scafall [ItemStackSnapshot]
 *
 * **Changes** made to the wrapped stack, once unwrapped, **won't be reflected on this stack**!
 */
fun net.minecraft.world.item.ItemStack.snapshot(): ItemStackSnapshot = ItemStackSnapshotCommon(this.copy())

/**
 * Unwraps a scafall [ItemStackLike] to a minecraft [net.minecraft.world.item.ItemStack]
 */
fun ItemStackLike.unwrap(): net.minecraft.world.item.ItemStack {
    if (this !is ItemStackLikeCommon) {
        throw IllegalArgumentException("Wrapped stack is not an instance of ${ItemStackLikeCommon::class.simpleName}")
    }

    return when (this) {
        is ScafallItemStackCommon -> {
            this.mcStack
        }

        is ItemStackSnapshotCommon -> {
            this.mcStack.copy()
        }
    }
}
