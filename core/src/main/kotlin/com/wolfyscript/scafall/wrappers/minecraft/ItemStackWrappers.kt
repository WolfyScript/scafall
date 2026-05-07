package com.wolfyscript.scafall.wrappers.minecraft

import com.wolfyscript.scafall.wrappers.world.items.*

/**
 * Wraps this Minecraft ItemStack in a scafall [ScafallItemStack].
 *
 * **Any changes** made to the wrapped stack, once unwrapped, **are reflected on this original stack**.
 */
fun net.minecraft.world.item.ItemStack.wrap(): ScafallItemStack = ScafallItemStackImpl.wrap(this)

/**
 * Wraps a snapshot of this ItemStack in a scafall [ItemStackSnapshot]
 *
 * **Changes** made to the wrapped stack, once unwrapped, **won't be reflected on this stack**!
 */
fun net.minecraft.world.item.ItemStack.snapshot(): ItemStackSnapshot = ItemStackSnapshotImpl.wrap(this)
