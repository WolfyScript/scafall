package com.wolfyscript.scafall.common.api.factories

import com.wolfyscript.scafall.factories.DataKeyFactory
import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.factories.IdentifierFactory
import com.wolfyscript.scafall.factories.RegistryFactory

abstract class CommonFactories : Factories {

    override val identifierFactory: IdentifierFactory = IdentifierFactoryImpl()

    override val dataKeyFactory: DataKeyFactory = DataKeyFactoryImpl()

    override val registryFactory: RegistryFactory = CommonRegistryFactory()

    abstract fun init()

}