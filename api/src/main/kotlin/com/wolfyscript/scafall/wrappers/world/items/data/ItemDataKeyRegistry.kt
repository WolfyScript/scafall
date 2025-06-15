package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.RegistrySimple
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike

class ItemDataKeyRegistry(key: Key) : RegistrySimple<DataKey<*, ItemStackLike<*,*>>>(key)

