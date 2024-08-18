package com.wolfyscript.scaffolding.spigot.platform.registry

import com.google.common.base.Preconditions
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scaffolding.registry.Registries
import com.wolfyscript.scaffolding.registry.RegistrySimple
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.BukkitStackIdentifier
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifier
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifierParser
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackReference
import org.bukkit.inventory.ItemStack
import java.util.*

class RegistryStackIdentifierParsers(registries: Registries) :
    RegistrySimple<StackIdentifierParser<*>>(
        scaffoldingKey("stack_identifiers/parsers"),
        registries,
        StackIdentifierParser::class.java
    ) {
    private var priorityIndexedParsers = listOf<StackIdentifierParser<*>>()

    override fun register(namespacedKey: Key, value: StackIdentifierParser<*>) {
        Preconditions.checkState(!map.containsKey(namespacedKey), "namespaced key '%s' already has an associated value!", namespacedKey)
        map[namespacedKey] = value
        reIndexParsers()
    }

    fun sortedParsers(): List<StackIdentifierParser<*>> {
        return priorityIndexedParsers
    }

    fun parseIdentifier(stack: ItemStack): StackIdentifier {
        for (parser in sortedParsers()) {
            val identifierOptional = parser.from(stack)
            if (identifierOptional != null) return identifierOptional
        }
        return BukkitStackIdentifier(stack)
    }

    fun matchingParsers(stack: ItemStack): List<StackIdentifierParser<*>> {
        return sortedParsers().stream().sorted()
            .filter { stackIdentifierParser -> stackIdentifierParser.from(stack) != null }
            .toList()
    }

    /**
     * @param stack
     * @return
     */
    fun parseFrom(stack: ItemStack?): Optional<StackReference> {
        if (stack == null) return Optional.empty()
        return Optional.of(StackReference(registries.core, stack.amount, 1.0, parseIdentifier(stack), stack))
    }

    private fun reIndexParsers() {
        val customPriorities: Map<Key, Int> =
            HashMap() // TODO: registries.getCore().getConfig().getIdentifierParserPriorities();

        priorityIndexedParsers = map.values.stream().filter { Objects.nonNull(it) }
            .sorted { parser, otherParser ->
                val parserPriority = customPriorities.getOrDefault(parser.key(), parser.priority())
                val otherPriority = customPriorities.getOrDefault(otherParser.key(), otherParser.priority())
                otherPriority.compareTo(parserPriority)
            }.toList()
    }
}
