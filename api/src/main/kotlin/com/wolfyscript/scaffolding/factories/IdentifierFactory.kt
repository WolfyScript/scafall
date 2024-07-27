package com.wolfyscript.scaffolding.factories

import com.wolfyscript.scaffolding.identifier.Key

interface IdentifierFactory {

    fun key(namespace: String, key: String): Key

    fun parse(string: String, separator: Char): Key

}