package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.identifier.Key

open class TypeRegistrySimple<V>(key: Key) : AbstractTypeRegistry<MutableMap<Key, Class<out V>>, V>(key, HashMap())
