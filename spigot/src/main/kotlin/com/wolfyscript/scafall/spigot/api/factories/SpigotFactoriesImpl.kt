package com.wolfyscript.scafall.spigot.api.factories

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.common.api.factories.CommonFactories
import com.wolfyscript.scafall.factories.ItemsFactory
import com.wolfyscript.scafall.factories.RegistryFactory

class SpigotFactoriesImpl(val scafall: Scafall) : CommonFactories() {

    override val itemsFactory: ItemsFactory = SpigotItemsFactoryImpl(scafall)

    override fun init() {}

}