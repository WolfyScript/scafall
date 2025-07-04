package com.wolfyscript.scafall.common.api.identifiers

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.google.common.base.Preconditions
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.KEY_REGEX
import com.wolfyscript.scafall.identifier.Key.Companion.NAMESPACE_REGEX
import net.minecraft.resources.ResourceLocation
import java.util.regex.Pattern

class KeyImpl @JsonCreator(mode = JsonCreator.Mode.DISABLED) internal constructor(override val namespace: String, override val value: String) : Key {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    internal constructor(key: String) : this(key.substringBefore(':', Key.SCAFFOLDING_NAMESPACE), key.substringAfter(':'))


    init {
        Preconditions.checkArgument(NAMESPACE_PATTERN.matcher(namespace).matches(), "Invalid namespace. Must be %s: %s", NAMESPACE_REGEX, namespace)
        Preconditions.checkArgument(KEY_PATTERN.matcher(value).matches(), "Invalid key. Must be %s: %s", KEY_REGEX, value)
    }

    override fun toAdventure(): net.kyori.adventure.key.Key {
        return net.kyori.adventure.key.Key.key(namespace, value)
    }

    override fun toMc(): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(namespace, value)
    }

    @JsonValue
    override fun toString(): String = "$namespace:$value"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KeyImpl) return false

        if (namespace != other.namespace) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = namespace.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

}

val NAMESPACE_PATTERN: Pattern = Pattern.compile(NAMESPACE_REGEX)
val KEY_PATTERN: Pattern = Pattern.compile(KEY_REGEX)
