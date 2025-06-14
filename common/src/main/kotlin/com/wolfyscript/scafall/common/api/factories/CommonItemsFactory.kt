package com.wolfyscript.scafall.common.api.factories

import com.mojang.serialization.Dynamic
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.factories.ItemsFactory
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import net.minecraft.SharedConstants
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.TagParser
import net.minecraft.util.datafix.DataFixers
import net.minecraft.util.datafix.fixes.References

class CommonItemsFactory(val scafall: Scafall) : ItemsFactory {

    override fun createStack(item: Key): ItemStack {
        TODO("Not yet implemented")
    }

    override fun createFromSNBT(snbt: String): ItemStack {
        val version = SharedConstants.getCurrentVersion().dataVersion.version
        return parseFromSNBT(snbt, version, version)
    }

    override fun parseFromSNBT(
        snbt: String,
        fromVersion: Int,
        toVersion: Int,
    ): ItemStack {
        val tag = TagParser.parseCompoundFully(snbt)

        // Update the stack using the data version if necessary
        val fixed = if (fromVersion < toVersion) {
            DataFixers.getDataFixer()
                .update(References.ITEM_STACK, Dynamic(NbtOps.INSTANCE, tag), fromVersion, toVersion)
                .value
        } else {
            tag
        }

        val stack = net.minecraft.world.item.ItemStack.parse(scafall.server.minecraftServer.registryAccess(), fixed)
        return stack.map { stack -> stack.wrap() }.orElseGet { net.minecraft.world.item.ItemStack.EMPTY.wrap() }
    }

}