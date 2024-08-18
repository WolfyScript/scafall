package com.wolfyscript.scaffolding.spigot.platform.world.items.reference

import com.wolfyscript.scaffolding.ScaffoldingProvider.Companion.get
import com.wolfyscript.scaffolding.identifier.Key
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
annotation class StackIdentifierParserSettings(
    /**
     * The priority of this parser.<br></br>
     * Parser with higher priority are called before Parsers with lower priority.<br></br>
     * Each time it chooses the next parser, if and only if the current parser returns null.<br></br>
     * The moment it returns a non-null value that value is used.
     *
     * @return The priority of this parser.
     */
    val priority: Short = 0,
    /**
     * The class of the custom Reference Parser
     *
     * @return The Parser for this ItemReference Type
     */
    val parser: KClass<out StackIdentifierParser<*>>, val plugin: String = ""
) {
    annotation class ParseMethod

    object Builder {
        fun <I : StackIdentifier> create(id: Key, identifierType: Class<I>): StackIdentifierParser<I>? {
            val annotation = identifierType.getAnnotation(
                StackIdentifierParserSettings::class.java
            )
            if (annotation == null) return null

            val potentialParserMethods = get().reflections.getMethodsAnnotatedWith(
                ParseMethod::class.java
            )
            for (potentialParserMethod in potentialParserMethods) {
                if (potentialParserMethod.parameterTypes.contentEquals(arrayOf<Class<*>>(ItemStack::class.java))) {
                }
            }

            return null
        }
    }
}
