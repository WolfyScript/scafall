package com.wolfyscript.scafall.common.api.factories

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.factories.DataKeyFactory
import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.factories.IdentifierFactory
import com.wolfyscript.scafall.factories.ItemsFactory
import com.wolfyscript.scafall.factories.RegistryFactory

abstract class CommonFactories(val scafall: Scafall) : Factories {

    override val identifierFactory: IdentifierFactory = IdentifierFactoryImpl()

    override val dataKeyFactory: DataKeyFactory = DataKeyFactoryImpl()

    override val registryFactory: RegistryFactory = CommonRegistryFactory()

    override val itemsFactory: ItemsFactory = CommonItemsFactory(scafall)

    abstract fun init()

}