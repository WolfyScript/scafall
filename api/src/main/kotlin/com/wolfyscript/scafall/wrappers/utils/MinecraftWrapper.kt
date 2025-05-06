package com.wolfyscript.scafall.wrappers.utils

import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike

/**
 * Wrapping utils that can wrap or unwrap to Minecraft's internal types.
 *
 * This may use a direct approach (Fabric) or use intermediate platform (Spigot, Sponge) wrappers to access the internal value.
 * For platforms like Sponge or Spigot, there exist separate api modules to wrap/unwrap those wrapper types instead of Minecraft's.
 */
interface MinecraftWrapper {

    /**
     * Wraps a Minecraft ItemStack into a Scafall ItemStack.
     *
     * @param mcStack The Minecraft ItemStack to wrap
     */
    fun wrapMcStack(mcStack: net.minecraft.world.item.ItemStack): ItemStack

    /**
     * Unwraps a Scafall ItemStack into a Minecraft ItemStack.
     *
     * @param wrappedStack The Scafall [ItemStack] or [ItemStackSnapshot][com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot] to unwrap
     */
    fun unwrapToMcStack(wrappedStack: ItemStackLike<*,*>): net.minecraft.world.item.ItemStack

}