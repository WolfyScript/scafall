package com.wolfyscript.scafall.common.api.registries

import com.wolfyscript.scafall.data.ItemDataComponentConverter
import com.wolfyscript.scafall.data.ItemDataComponentConverterRegistry
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.Registries
import com.wolfyscript.scafall.registry.RegistrySimple

class ItemDataComponentConverterRegistryImpl(registries: Registries) : ItemDataComponentConverterRegistry,
    RegistrySimple<ItemDataComponentConverter<*>>(
        Key.defaultKey("data_component/item/converter"),
        registries
    )