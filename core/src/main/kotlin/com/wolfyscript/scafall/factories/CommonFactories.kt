package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.Scafall

abstract class CommonFactories(val scafall: Scafall) : Factories {

    override val registryFactory: RegistryFactory = CommonRegistryFactory()

    override val itemsFactory: ItemsFactory = CommonItemsFactory(scafall)

    abstract fun init()

}