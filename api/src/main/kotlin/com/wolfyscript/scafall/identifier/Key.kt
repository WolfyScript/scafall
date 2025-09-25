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

        /**
         * Creates a new Key with the scafall namespace
         */
        @JvmStatic
        fun scafall(key: String): Key = key(SCAFFOLDING_NAMESPACE, key)

        /**
         * Creates a new Key with the minecraft namespace
         */
        @JvmStatic
        fun minecraft(key: String): Key = key(MINECRAFT_NAMESPACE, key)

        /**
         * Creates a new Key with the specified namespace and key
         */
        @JvmStatic
        fun key(namespace: String, key: String): Key = ScafallProvider.get().factories.identifierFactory.key(namespace, key)

        /**
         * Creates a new Key with the specified namespace of the namespaced object and key
         */
        @JvmStatic
        fun key(namespaced: Namespaced, key: String): Key = key(namespaced.namespace, key)

        /**
         * Parses a key from a string of the format `<namespace><separator><key>`
         */
        @JvmStatic
        fun parse(string: String, separator: Char): Key = ScafallProvider.get().factories.identifierFactory.parse(string, separator)

        /**
         * Parses a key from a string of the format `<namespace>:<key>`
         */
        @JvmStatic
        fun parse(string: String): Key = parse(string, SEPARATOR)

        /**
         * Creates a new Key from a minecraft ResourceLocation
         */
        @JvmStatic
        fun fromMc(location: ResourceLocation): Key = key(location.namespace, location.path)

    }

    val value: String

    /**
     * Creates a new Adventure [Key][net.kyori.adventure.key.Key] from this key
     */
    fun toAdventure() : net.kyori.adventure.key.Key

    /**
     * Creates a new Minecraft [ResourceLocation] from this key
     */
    fun toMc() : ResourceLocation

    override fun toString(): String

}

/**
 * Creates a new Key from a minecraft ResourceLocation
 */
fun ResourceLocation.toKey(): Key = Key.fromMc(this)
