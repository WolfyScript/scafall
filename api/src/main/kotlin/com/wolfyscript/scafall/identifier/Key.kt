package com.wolfyscript.scafall.identifier

import com.wolfyscript.scafall.ScafallProvider
import net.minecraft.resources.ResourceLocation
import org.intellij.lang.annotations.RegExp

interface Key : Namespaced {

    companion object {

        const val SCAFFOLDING_NAMESPACE = "scaffolding"
        const val MINECRAFT_NAMESPACE = "minecraft"

        const val SEPARATOR = ':'
        @field:RegExp const val NAMESPACE_REGEX = "[a-z0-9._-]+"
        @field:RegExp const val KEY_REGEX = "[a-z0-9/._-]+"

        @JvmStatic
        fun defaultKey(key: String): Key = key(SCAFFOLDING_NAMESPACE, key)

        @JvmStatic
        fun minecraft(key: String): Key = key(MINECRAFT_NAMESPACE, key)

        @JvmStatic
        fun key(namespace: String, key: String): Key = ScafallProvider.get().factories.identifierFactory.key(namespace, key)

        @JvmStatic
        fun key(namespaced: Namespaced, key: String): Key = key(namespaced.namespace, key)

        @JvmStatic
        fun parse(string: String, separator: Char): Key = ScafallProvider.get().factories.identifierFactory.parse(string, separator)

        @JvmStatic
        fun parse(string: String): Key = parse(string, SEPARATOR)

        @JvmStatic
        fun fromMc(location: ResourceLocation): Key = key(location.namespace, location.path)

    }

    val value: String

    fun toAdventure() : net.kyori.adventure.key.Key

    fun toMc() : ResourceLocation

    override fun toString(): String

}

fun ResourceLocation.toKey(): Key = Key.fromMc(this)
