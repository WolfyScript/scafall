package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.items.ItemStackRef
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import net.minecraft.SharedConstants
import net.minecraft.world.item.Item

interface ItemsFactory {

    fun createStack(item: Key) : ItemStack

    /**
     * Creates the [ItemStack] from the given SNBT without updating the NBT beforehand.
     *
     * @see parseFromSNBT
     */
    fun createFromSNBT(snbt: String) : ItemStack

    /**
     * Parses the [ItemStack] from the given SNBT.
     * It first parses the SNBT to NBT, which is then updated using [com.mojang.datafixers.DataFixerUpper] if necessary.
     * Then it parses the ItemStack from the updated NBT CompoundTag.
     */
    fun parseFromSNBT(snbt: String, fromVersion: Int, toVersion: Int = SharedConstants.getCurrentVersion().dataVersion.version) : ItemStack

    /**
     * Directly creates a [ItemStackRef] with a [com.wolfyscript.scafall.items.ItemStackIdentifier] that references the specified stack,
     * without parsing it.
     */
    fun createVanillaStackRef(stack: ItemStack, count: Int = stack.amount): ItemStackRef

    /**
     * Directly creates a [ItemStackRef] with a [com.wolfyscript.scafall.items.ItemStackIdentifier] that references a stack of the specified item.
     */
    fun createVanillaStackRef(item: Item, count: Int = 1): ItemStackRef

    /**
     * Parses the [com.wolfyscript.scafall.items.ItemStackIdentifier] from the specified stack using the registered
     * [com.wolfyscript.scafall.registry.Registries.itemStackIdentifierParsers].
     *
     * Parsers of higher priority take precedence.
     */
    fun parseStackRef(stack: ItemStack, count: Int = stack.amount): ItemStackRef?

}