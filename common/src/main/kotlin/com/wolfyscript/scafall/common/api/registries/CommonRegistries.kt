package com.wolfyscript.scafall.common.api.registries

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.data.ItemStackDataComponentConverterRegistry
import com.wolfyscript.scafall.registry.Registries

class CommonRegistries(scafall: Scafall) : Registries(scafall) {

    override val itemStackDataComponentConverterRegistry: ItemStackDataComponentConverterRegistry = ItemStackDataComponentConverterRegistryImpl(this)

}