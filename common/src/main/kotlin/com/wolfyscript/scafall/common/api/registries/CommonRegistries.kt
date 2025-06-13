package com.wolfyscript.scafall.common.api.registries

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.common.api.items.VanillaItemStackIdentifier
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.Registries

class CommonRegistries(scafall: Scafall) : Registries(scafall) {

    init {

        // TODO: put this here for now, should put it somewhere else though
        itemStackIdentifiers.register(Key.key(Key.SCAFFOLDING_NAMESPACE, "vanilla"), VanillaItemStackIdentifier::class.java)
    }

}