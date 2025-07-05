package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.identifier.Key

/**
 * Used to create [Key] instances.
 */
interface IdentifierFactory {

    fun key(namespace: String, key: String): Key

    fun parse(string: String, separator: Char): Key

}