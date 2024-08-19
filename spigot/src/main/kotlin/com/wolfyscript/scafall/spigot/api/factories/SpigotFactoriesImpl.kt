package com.wolfyscript.scafall.spigot.api.factories

import com.wolfyscript.scafall.common.api.factories.CommonFactories
import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.factories.ItemsFactory

class SpigotFactoriesImpl : CommonFactories() {

    override val itemsFactory: ItemsFactory = SpigotItemsFactoryImpl()

}