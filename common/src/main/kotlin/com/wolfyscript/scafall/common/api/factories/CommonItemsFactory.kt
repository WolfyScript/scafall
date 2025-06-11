package com.wolfyscript.scafall.common.api.factories

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.factories.ItemsFactory
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import net.minecraft.nbt.TagParser

class CommonItemsFactory(val scafall: Scafall) : ItemsFactory {

    override fun createStack(item: Key): ItemStack {
        TODO("Not yet implemented")
    }

    override fun createFromSNBT(snbt: String): ItemStack {
        val tag = TagParser.parseCompoundFully(snbt)
        val stack = net.minecraft.world.item.ItemStack.parse(scafall.server.minecraftServer.registryAccess(), tag)

        return stack.map { stack -> stack.wrap() }.orElseGet { net.minecraft.world.item.ItemStack.EMPTY.wrap() }
    }

}