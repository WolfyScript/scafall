package com.wolfyscript.scafall.sponge.api.wrappers.world.items

import com.wolfyscript.scafall.common.api.data.ItemStackDataComponentMap
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.wrappers.SpongeRefWrapper
import com.wolfyscript.scafall.sponge.api.wrappers.wrap
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import org.spongepowered.api.data.persistence.DataFormats
import org.spongepowered.api.item.ItemTypes
import java.io.ByteArrayOutputStream

class SpongeItemStack(ref: org.spongepowered.api.item.inventory.ItemStack) : SpongeRefWrapper<org.spongepowered.api.item.inventory.ItemStack>(ref), ItemStack {

    private val componentMap = ItemStackDataComponentMap(this)

    override val item: Key = ItemTypes.registry().valueKey(ref.type()).toAPI()
    override val amount: Int = ref.quantity()

    override fun toNBTString(): String {
        return DataFormats.SNBT.get().write(ref.get()!!.toContainer())
    }

    override fun toNBTBytes(): ByteArray {
        val stream = ByteArrayOutputStream()
        stream.use {
            DataFormats.NBT.get().writeTo(it, ref.get()!!.toContainer())
        }
        return stream.toByteArray()
    }

    override fun snapshot(): ItemStackSnapshot {
        return ref.get()!!.asImmutable().wrap()
    }

    override val data: DataComponentMap.Mutable<ItemStack> = componentMap

    override fun toString() : String = toNBTString()
}