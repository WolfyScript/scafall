package com.wolfyscript.scafall.spigot.api.identifiers

import com.wolfyscript.scafall.identifier.Key
import org.bukkit.NamespacedKey

fun NamespacedKey.api() : Key {
    return Key.key(namespace, key)
}

fun Key.bukkit() : NamespacedKey {
    return NamespacedKey(namespace, value)
}