package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import net.minecraft.SharedConstants

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

}