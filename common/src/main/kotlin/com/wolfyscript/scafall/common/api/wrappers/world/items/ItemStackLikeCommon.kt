package com.wolfyscript.scafall.common.api.wrappers.world.items

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtIo
import java.io.ByteArrayOutputStream

sealed class ItemStackLikeCommon<H: ItemStackLike<H, M>, M: DataComponentMap<H>>(val mcStack: net.minecraft.world.item.ItemStack) : ItemStackLike<H, M> {

    override val item: Key
        get() {
            val mcKey = BuiltInRegistries.ITEM.getKey(mcStack.item)
            return Key.key(mcKey.namespace, mcKey.path)
        }

    override val amount: Int
        get() = mcStack.count

    override val isEmpty: Boolean
        get() = mcStack.isEmpty

    override fun toNBTString(): String {
        val registryAccess = ScafallProvider.get().server.minecraftServer.registryAccess()
        return mcStack.save(registryAccess).toString()
    }

    override fun toNBTBytes(): ByteArray {
        val registryAccess = ScafallProvider.get().server.minecraftServer.registryAccess()

        val tag = CompoundTag()
        val stream = ByteArrayOutputStream()
        mcStack.save(registryAccess, tag)

        stream.use {
            NbtIo.writeCompressed(tag, it)
        }

        return stream.toByteArray()
    }

    override fun toString(): String {
        return "ItemStackLikeCommon(mcStack=$mcStack, amount=$amount)"
    }

}