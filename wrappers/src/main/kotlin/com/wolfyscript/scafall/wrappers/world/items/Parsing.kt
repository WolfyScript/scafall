package com.wolfyscript.scafall.wrappers.world.items

import com.mojang.serialization.Dynamic
import com.wolfyscript.scafall.wrappers.minecraft.wrap
import net.minecraft.SharedConstants
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.TagParser
import net.minecraft.util.datafix.DataFixers
import net.minecraft.util.datafix.fixes.References
import net.minecraft.world.item.ItemStack

fun parseFromSNBT(
    snbt: String,
    fromVersion: Int,
    toVersion: Int = SharedConstants.getCurrentVersion().dataVersion().version,
): ScafallItemStack {
    val tag = TagParser.parseCompoundFully(snbt)

    // Update the stack using the data version if necessary
    val fixed = if (fromVersion < toVersion) {
        DataFixers.getDataFixer()
            .update(References.ITEM_STACK, Dynamic(NbtOps.INSTANCE, tag), fromVersion, toVersion)
            .value
    } else {
        tag
    }

    return ItemStack.CODEC
        .parse(Dynamic(NbtOps.INSTANCE, fixed))
        .result()
        .map { it.wrap() }.orElseGet { ItemStack.EMPTY.wrap() }
}