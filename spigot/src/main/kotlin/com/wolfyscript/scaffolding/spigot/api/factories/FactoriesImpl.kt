package com.wolfyscript.scaffolding.spigot.api.factories

import com.wolfyscript.scaffolding.factories.Factories
import com.wolfyscript.scaffolding.factories.IdentifierFactory

abstract class FactoriesImpl : Factories {

    override val identifierFactory: IdentifierFactory = IdentifierFactoryImpl()

}