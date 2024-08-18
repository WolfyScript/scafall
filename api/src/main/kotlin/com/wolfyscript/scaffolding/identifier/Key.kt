package com.wolfyscript.scaffolding.identifier

import com.wolfyscript.scaffolding.ScaffoldingProvider
import org.intellij.lang.annotations.RegExp

interface Key : Namespaced {

    companion object {

        const val SCAFFOLDING_NAMESPACE = "scaffolding"
        const val MINECRAFT_NAMESPACE = "minecraft"

        const val SEPARATOR = ':'
        @field:RegExp const val NAMESPACE_REGEX = "[a-z0-9._-]+"
        @field:RegExp const val KEY_REGEX = "[a-z0-9._-]+"

        @JvmStatic
        fun scaffoldingKey(key: String): Key = key(SCAFFOLDING_NAMESPACE, key)

        @JvmStatic
        fun key(namespace: String, key: String): Key = ScaffoldingProvider.get().factories.identifierFactory.key(namespace, key)

        @JvmStatic
        fun key(namespaced: Namespaced, key: String): Key = key(namespaced.namespace, key)

        @JvmStatic
        fun parse(string: String, separator: Char): Key = ScaffoldingProvider.get().factories.identifierFactory.parse(string, separator)

        @JvmStatic
        fun parse(string: String): Key = parse(string, SEPARATOR)

    }

    val value: String

    fun into() : net.kyori.adventure.key.Key

}