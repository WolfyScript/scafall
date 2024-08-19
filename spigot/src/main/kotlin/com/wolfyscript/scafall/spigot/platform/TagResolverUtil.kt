package com.wolfyscript.scafall.spigot.platform

import com.wolfyscript.scafall.ScafallProvider.Companion.get
import com.wolfyscript.scafall.spigot.api.compatibilityManager
import com.wolfyscript.scafall.spigot.platform.compatibility.plugins.PlaceholderAPIIntegration
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.Context
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.entity.Player
import java.util.function.Function
import java.util.function.Supplier

object TagResolverUtil {
    fun papi(player: Player?): TagResolver {
        return TagResolver.resolver(
            "papi"
        ) { args: ArgumentQueue, context: Context? ->
            var text =
                args.popOr("The <papi> tag requires exactly one argument, text with papi placeholders!").value()
            val integration: PlaceholderAPIIntegration? = get().compatibilityManager.plugins.getIntegration("PlaceholderAPI", PlaceholderAPIIntegration::class.java)
            if (integration != null) {
                text = integration.setPlaceholders(player, text)
            }
            Tag.inserting(Component.text(text))
        }
    }

    fun entries(descriptionComponents: List<Component?>, shift: Int): TagResolver {
        return entries(descriptionComponents, Component.empty(), shift)
    }

    /**
     * Creates a [TagResolver], that will replace tags of the syntax: <br></br>
     * `<list_entry:<index>>`<br></br>
     * This will insert the Component from the specified list and index.<br></br>
     * In case the specific index is outbounds, it'll insert an empty Component.<br></br>
     * If no index is specified the tag will resolve to the first element of the components list.<br></br>
     * If the list itself is empty, it'll insert an empty Component.
     *
     * @param descriptionComponents The Components to use for the resolver.
     * @return The new TagResolver that resolves the `<entries:<index>>` tags.
     */
    @JvmOverloads
    fun entries(
        descriptionComponents: List<Component?>,
        emptyComponent: Component = Component.empty(),
        shift: Int = 0
    ): TagResolver {
        return entries(
            descriptionComponents,
            { integer: Int? -> Tag.inserting(emptyComponent) },
            { Tag.inserting(emptyComponent) }, shift
        )
    }

    fun entries(
        descriptionComponents: List<Component?>,
        outBoundsComponent: Component,
        emptyComponent: Component,
        shift: Int
    ): TagResolver {
        return entries(
            descriptionComponents,
            { integer: Int? -> Tag.inserting(outBoundsComponent) },
            { Tag.inserting(emptyComponent) }, shift
        )
    }

    fun entries(
        descriptionComponents: List<Component?>,
        outBoundsTag: Function<Int?, Tag>,
        emptyTag: Supplier<Tag>,
        shift: Int
    ): TagResolver {
        return entries("list_entry", descriptionComponents, outBoundsTag, emptyTag, shift)
    }

    fun entries(
        tagName: String,
        descriptionComponents: List<Component?>,
        outBoundsTag: Function<Int?, Tag>,
        emptyTag: Supplier<Tag>,
        shift: Int
    ): TagResolver {
        return TagResolver.resolver(
            tagName
        ) { argumentQueue: ArgumentQueue, context: Context? ->
            if (descriptionComponents.isEmpty()) return@resolver emptyTag.get()
            var index = if (argumentQueue.hasNext()) argumentQueue.pop().asInt().orElse(0) else 0
            if (index >= descriptionComponents.size) return@resolver outBoundsTag.apply(index)
            index = (index + shift) % descriptionComponents.size
            Tag.inserting(descriptionComponents[index]!!)
        }
    }
}
