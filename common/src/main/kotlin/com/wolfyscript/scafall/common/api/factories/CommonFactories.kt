package com.wolfyscript.scafall.common.api.factories

import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.factories.IdentifierFactory

abstract class CommonFactories : Factories {

    override val identifierFactory: IdentifierFactory = IdentifierFactoryImpl()

}