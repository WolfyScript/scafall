package com.wolfyscript.scafall.sponge.api.factories

import com.wolfyscript.scafall.factories.ItemsFactory
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.SpongeItemStackConfig
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig

class SpongeItemsFactory : ItemsFactory {

    override fun createStackConfig(itemKey: Key): ItemStackConfig {
        return SpongeItemStackConfig(itemKey.toString())
    }
}