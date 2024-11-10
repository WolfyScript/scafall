package com.wolfyscript.scafall.common.api.registries

import com.wolfyscript.scafall.data.ItemStackDataComponentConverter
import com.wolfyscript.scafall.data.ItemStackDataComponentConverterRegistry
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.Registries
import com.wolfyscript.scafall.registry.RegistrySimple

class ItemStackDataComponentConverterRegistryImpl(registries: Registries) : ItemStackDataComponentConverterRegistry,
    RegistrySimple<ItemStackDataComponentConverter<*>>(
        Key.defaultKey("itemstack/data_component_converters"),
        registries
    )