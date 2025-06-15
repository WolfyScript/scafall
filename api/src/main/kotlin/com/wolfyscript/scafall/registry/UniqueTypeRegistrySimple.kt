package com.wolfyscript.scafall.registry

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.wolfyscript.scafall.identifier.Key

open class UniqueTypeRegistrySimple<V>(key: Key) : AbstractTypeRegistry<BiMap<Key, Class<out V>>, V>(key, HashBiMap.create())
