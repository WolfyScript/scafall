package com.wolfyscript.scafall.sponge.api.factories

import com.wolfyscript.scafall.data.DataKeyProvider
import com.wolfyscript.scafall.factories.ItemsFactory
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.data.SpongeItemStackDataKeyProvider
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.SpongeItemStackConfig
import com.wolfyscript.scafall.sponge.api.wrappers.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.persistence.DataFormats

class SpongeItemsFactory : ItemsFactory {

    override fun createStackConfig(itemKey: Key): ItemStackConfig {
        return SpongeItemStackConfig(itemKey.toString())
    }

    override fun createFromSNBT(snbt: String): ItemStack {
        val container = DataFormats.SNBT.get().read(snbt)
        val stack = Sponge.dataManager().deserialize(org.spongepowered.api.item.inventory.ItemStack::class.java, container)
        return stack.map { it.wrap() }.orElseThrow { RuntimeException("Could not deserialize snbt: $snbt") }
    }

    override val dataKeyProvider: DataKeyProvider = SpongeItemStackDataKeyProvider()
}