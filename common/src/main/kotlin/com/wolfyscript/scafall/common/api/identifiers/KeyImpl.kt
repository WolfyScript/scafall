package com.wolfyscript.scafall.common.api.identifiers

import com.google.common.base.Preconditions
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.KEY_REGEX
import com.wolfyscript.scafall.identifier.Key.Companion.NAMESPACE_REGEX
import java.util.regex.Pattern

class KeyImpl(override val namespace: String, override val value: String) : Key {

    init {
        Preconditions.checkArgument(NAMESPACE_PATTERN.matcher(namespace).matches(), "Invalid namespace. Must be %s: %s", NAMESPACE_REGEX, namespace)
        Preconditions.checkArgument(KEY_PATTERN.matcher(value).matches(), "Invalid key. Must be %s: %s", KEY_REGEX, value)
    }

    override fun into(): net.kyori.adventure.key.Key {
        return net.kyori.adventure.key.Key.key(namespace, value)
    }

    override fun toString(): String = "$namespace:$value"

}

val NAMESPACE_PATTERN: Pattern = Pattern.compile(NAMESPACE_REGEX)
val KEY_PATTERN: Pattern = Pattern.compile(KEY_REGEX)
