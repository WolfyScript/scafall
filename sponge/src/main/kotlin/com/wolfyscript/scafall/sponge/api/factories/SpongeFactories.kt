package com.wolfyscript.scafall.sponge.api.factories

import com.wolfyscript.scafall.common.api.factories.DataKeyFactoryImpl
import com.wolfyscript.scafall.common.api.factories.IdentifierFactoryImpl
import com.wolfyscript.scafall.factories.DataKeyFactory
import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.factories.IdentifierFactory
import com.wolfyscript.scafall.factories.ItemsFactory

class SpongeFactories : Factories {

    override val identifierFactory: IdentifierFactory
        get() = IdentifierFactoryImpl()
    override val itemsFactory: ItemsFactory
        get() = SpongeItemsFactory()
    override val dataKeyFactory: DataKeyFactory
        get() = DataKeyFactoryImpl()
}