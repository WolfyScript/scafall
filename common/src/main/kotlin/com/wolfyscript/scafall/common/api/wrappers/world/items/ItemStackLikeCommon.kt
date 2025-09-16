package com.wolfyscript.scafall.common.api.wrappers.world.items

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.NbtOps
import net.minecraft.world.item.ItemStack
import java.io.ByteArrayOutputStream
import kotlin.jvm.optionals.getOrNull

sealed class ItemStackLikeCommon : ItemStackLike {

    @JsonIgnore
    val mcStack: ItemStack

    protected constructor(mcStack: ItemStack) {
        this.mcStack = mcStack
    }

    @get:JsonIgnore
    override val item: Key
        get() {
            val mcKey = BuiltInRegistries.ITEM.getKey(mcStack.item)
            return Key.key(mcKey.namespace, mcKey.path)
        }

    @get:JsonIgnore
    override val amount: Int
        get() = mcStack.count

    @get:JsonIgnore
    override val isEmpty: Boolean
        get() = mcStack.isEmpty

    override fun toNBTString(): String {
        val registryAccess = ScafallProvider.get().server.minecraftServer.registryAccess()
        val result = ItemStack.SINGLE_ITEM_CODEC.encodeStart(registryAccess.createSerializationContext(NbtOps.INSTANCE), mcStack)
        // TODO: handle errors
        return result.result().map { it.toString() }.orElse("")
    }

    override fun toNBTBytes(): ByteArray {
        val stream = ByteArrayOutputStream()

        val registryAccess = ScafallProvider.get().server.minecraftServer.registryAccess()
        val result = ItemStack.SINGLE_ITEM_CODEC.encodeStart(registryAccess.createSerializationContext(NbtOps.INSTANCE), mcStack).result().getOrNull()

        if (result == null || result !is CompoundTag) {
            return ByteArray(0)
        }

        stream.use {
            NbtIo.writeCompressed(result, it)
        }
        return stream.toByteArray()
    }

    override fun toString(): String {
        return "$mcStack [${mcStack.componentsPatch}]"
    }

}