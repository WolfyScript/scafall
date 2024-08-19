package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.identifier.Key

interface IdentifierFactory {

    fun key(namespace: String, key: String): Key

    fun parse(string: String, separator: Char): Key

}