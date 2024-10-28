package com.wolfyscript.scafall.spigot.api.factories

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.common.api.factories.CommonFactories
import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.factories.ItemsFactory

class SpigotFactoriesImpl(val scafall: Scafall) : CommonFactories() {

    override lateinit var itemsFactory: ItemsFactory

    override fun init() {
        itemsFactory = SpigotItemsFactoryImpl(scafall)
    }

}