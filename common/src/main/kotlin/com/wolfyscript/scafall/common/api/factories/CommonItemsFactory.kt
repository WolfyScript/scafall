package com.wolfyscript.scafall.common.api.factories

import com.mojang.serialization.Dynamic
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.common.api.items.ItemStackRefImpl
import com.wolfyscript.scafall.common.api.items.VanillaItemStackIdentifier
import com.wolfyscript.scafall.factories.ItemsFactory
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.items.ItemStackRef
import com.wolfyscript.scafall.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import net.minecraft.SharedConstants
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.TagParser
import net.minecraft.util.datafix.DataFixers
import net.minecraft.util.datafix.fixes.References
import net.minecraft.world.item.Item

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

    override fun createVanillaStackRef(stack: ItemStack, count: Int): ItemStackRef {
        return ItemStackRefImpl(count, VanillaItemStackIdentifier(stack))
    }

    override fun createVanillaStackRef(
        item: Item,
        count: Int,
    ): ItemStackRef {
        return createVanillaStackRef(net.minecraft.world.item.ItemStack(item).wrap(), count)
    }

    override fun parseStackRef(stack: ItemStack, count: Int): ItemStackRef? {
        val parsers = ScafallProvider.get().registries.itemStackIdentifierParsers.values().sortedByDescending { it.priority }
        val identifier = parsers.firstNotNullOfOrNull {
            it.from(stack)
        }
        if (identifier == null) {
            return null
        }
        return ItemStackRefImpl(count, identifier)
    }

}