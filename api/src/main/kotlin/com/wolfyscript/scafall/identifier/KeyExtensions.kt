package com.wolfyscript.scafall.identifier

import com.wolfyscript.scafall.identifier.Key.Companion.KEY_REGEX
import com.wolfyscript.scafall.identifier.Key.Companion.NAMESPACE_REGEX
import net.minecraft.resources.Identifier
import java.util.regex.Pattern

internal val NAMESPACE_PATTERN: Pattern = Pattern.compile(NAMESPACE_REGEX)
internal val KEY_PATTERN: Pattern = Pattern.compile(KEY_REGEX)

/**
 * Creates a new Key from a minecraft ResourceLocation
 */
fun Identifier.toKey(): Key = Key.fromMc(this)

/**
 * Converts an Adventure key to a scafall [com.wolfyscript.scafall.identifier.Key]
 */
fun net.kyori.adventure.key.Key.toScafall() : Key = Key.key(this.namespace(), this.value())

/**
 * Converts a minecraft [ResourceLocation] to a scafall [com.wolfyscript.scafall.identifier.Key]
 */
fun Identifier.toScafall() : Key = Key.key(this.namespace, this.path)