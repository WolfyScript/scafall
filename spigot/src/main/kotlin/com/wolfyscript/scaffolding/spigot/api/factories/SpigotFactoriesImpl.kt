package com.wolfyscript.scaffolding.spigot.api.factories

import com.wolfyscript.scaffolding.common.api.factories.CommonFactories
import com.wolfyscript.scaffolding.factories.Factories
import com.wolfyscript.scaffolding.factories.ItemsFactory

class SpigotFactoriesImpl : CommonFactories() {

    override val itemsFactory: ItemsFactory = SpigotItemsFactoryImpl()

}