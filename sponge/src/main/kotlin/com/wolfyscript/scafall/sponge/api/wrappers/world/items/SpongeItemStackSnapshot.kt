package com.wolfyscript.scafall.sponge.api.wrappers.world.items

import com.wolfyscript.scafall.common.api.data.ItemStackSnapshotDataComponentMap
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import org.spongepowered.api.data.persistence.DataFormats
import org.spongepowered.api.item.ItemTypes
import java.io.ByteArrayOutputStream

class SpongeItemStackSnapshot(val ref: org.spongepowered.api.item.inventory.ItemStackSnapshot) : ItemStackSnapshot {

    private val componentMap = ItemStackSnapshotDataComponentMap(this)

    override fun createStack(): ItemStack {
        return SpongeItemStack(ref.asMutable())
    }

    override fun toNBTString(): String {
        return DataFormats.SNBT.get().write(ref.toContainer())
    }

    override fun toNBTBytes(): ByteArray {
        val stream = ByteArrayOutputStream()
        stream.use {
            DataFormats.NBT.get().writeTo(it, ref.toContainer())
        }
        return stream.toByteArray()
    }

    override val item: Key = ItemTypes.registry().valueKey(ref.type()).toAPI()
    override val amount: Int = ref.quantity()

    override val data: DataComponentMap.Immutable<ItemStackSnapshot> = componentMap

}