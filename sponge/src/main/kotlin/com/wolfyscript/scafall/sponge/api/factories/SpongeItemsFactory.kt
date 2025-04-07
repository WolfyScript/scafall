package com.wolfyscript.scafall.sponge.api.factories

import com.wolfyscript.scafall.factories.ItemsFactory
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.wrappers.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import net.kyori.adventure.text.StorageNBTComponent
import org.spongepowered.api.ResourceKey
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.persistence.DataContainer
import org.spongepowered.api.data.persistence.DataFormats
import org.spongepowered.api.data.persistence.DataSerializable
import org.spongepowered.api.item.ItemType
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStackLike

class SpongeItemsFactory : ItemsFactory {

    override fun createStack(item: Key): ItemStack {
        return ItemTypes.registry().findValue<ItemType>(ResourceKey.resolve(item.toString())).map {
            org.spongepowered.api.item.inventory.ItemStack.of(it).wrap()
        }.orElseThrow { IllegalArgumentException("Cannot create stack of type $item") }
    }

    override fun createFromSNBT(snbt: String): ItemStack {

        val container = DataFormats.SNBT.get().read(snbt)
        val stack = Sponge.dataManager().deserialize(ItemStackLike::class.java, container)
        return stack.map { it.asMutableCopy().wrap() }.orElseThrow { RuntimeException("Could not deserialize snbt: $snbt") }
    }

}