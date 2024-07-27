package com.wolfyscript.scaffolding.common.api.factories

import com.google.common.base.Preconditions
import com.wolfyscript.scaffolding.common.api.identifiers.KeyImpl
import com.wolfyscript.scaffolding.factories.IdentifierFactory
import com.wolfyscript.scaffolding.identifier.Key

class IdentifierFactoryImpl : IdentifierFactory {

    override fun key(namespace: String, key: String): Key = KeyImpl(namespace, key)

    override fun parse(string: String, separator: Char): Key {
        Preconditions.checkArgument(string.length < 256, "NamespacedKey must be less than 256 characters (%s)", string)
        val split = string.split(separator)
        if (split.size != 2) {
            throw IllegalArgumentException("$string is not a valid key")
        }
        return KeyImpl(split[0], split[1])
    }

}