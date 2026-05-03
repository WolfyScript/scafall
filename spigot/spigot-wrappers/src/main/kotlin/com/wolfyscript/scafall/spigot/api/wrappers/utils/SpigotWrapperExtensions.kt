package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.identifier.Key
import org.bukkit.NamespacedKey

//
// Key converters
//

// TODO: Move this to identifier extension utils

/**
 * Converts this [Key] to a Spigot [NamespacedKey]
 */
fun Key.toSpigot() : NamespacedKey {
    return org.bukkit.NamespacedKey(namespace, value)
}

/**
 * Converts this Spigot [NamespacedKey] to a scafall [Key]
 */
fun NamespacedKey.toScafall() : Key {
    return Key.key(namespace, key)
}
