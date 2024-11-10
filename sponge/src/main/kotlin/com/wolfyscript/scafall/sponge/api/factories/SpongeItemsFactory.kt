package com.wolfyscript.scafall.sponge.api.factories

import com.wolfyscript.scafall.data.DataKeyProvider
import com.wolfyscript.scafall.factories.ItemsFactory
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.data.SpongeItemStackDataKeyProvider
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.SpongeItemStackConfig
import com.wolfyscript.scafall.sponge.api.wrappers.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import org.spongepowered.api.ResourceKey
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.persistence.DataFormats
import org.spongepowered.api.item.ItemType
import org.spongepowered.api.item.ItemTypes
import kotlin.jvm.optionals.getOrNull

class SpongeItemsFactory : ItemsFactory {

    override fun createStack(item: Key): ItemStack {
        return ItemTypes.registry().findValue<ItemType>(ResourceKey.resolve(item.toString())).map {
            org.spongepowered.api.item.inventory.ItemStack.of(it).wrap()
        }.orElseThrow { IllegalArgumentException("Cannot create stack of type $item") }
    }

    override fun createFromSNBT(snbt: String): ItemStack {
        val container = DataFormats.SNBT.get().read(snbt)
        val stack = Sponge.dataManager().deserialize(org.spongepowered.api.item.inventory.ItemStack::class.java, container)
        return stack.map { it.wrap() }.orElseThrow { RuntimeException("Could not deserialize snbt: $snbt") }
    }

    override val dataKeyProvider: DataKeyProvider = SpongeItemStackDataKeyProvider()
}