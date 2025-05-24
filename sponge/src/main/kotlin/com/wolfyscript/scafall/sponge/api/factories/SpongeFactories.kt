package com.wolfyscript.scafall.sponge.api.factories

import com.wolfyscript.scafall.common.api.factories.CommonFactories
import com.wolfyscript.scafall.common.api.factories.DataKeyFactoryImpl
import com.wolfyscript.scafall.common.api.factories.IdentifierFactoryImpl
import com.wolfyscript.scafall.factories.DataKeyFactory
import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.factories.IdentifierFactory
import com.wolfyscript.scafall.factories.ItemsFactory

class SpongeFactories : CommonFactories() {

    override val itemsFactory: ItemsFactory = SpongeItemsFactory()

    override fun init() {

    }
}