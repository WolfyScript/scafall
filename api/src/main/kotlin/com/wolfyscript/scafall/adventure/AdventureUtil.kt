package com.wolfyscript.scafall.adventure

import com.wolfyscript.scafall.ScafallProvider
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentBuilder
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.minecraft.network.chat.MutableComponent
import java.util.*

/**
 * A wrapper for the Adventure API Audiences.
 *
 * This wrapper tries to provide cross-platform audiences as well as possible.
 * The platform specific implementation must be used in cases where this wrapper may be lacking features or audiences!
 */
interface AdventureUtil {

    fun player(uuid: UUID) : Audience

    fun all() : Audience

    fun system() : Audience

    val miniMsg: MiniMessage
        get() = MiniMessage.miniMessage()

    fun toVanilla(component: Component): MutableComponent

}

fun Key.toAPI() : com.wolfyscript.scafall.identifier.Key = ScafallProvider.Companion.get().factories.identifierFactory.key(this.namespace(), this.value())

/* ****************************************** *
 *  Util extension functions for MiniMessage  *
 * ****************************************** */

fun String.deser(tagResolver: TagResolver = TagResolver.empty(), miniMsg: MiniMessage = MiniMessage.miniMessage()) = miniMsg.deserialize(this, tagResolver)

fun String.deser(vararg tagResolver: TagResolver = emptyArray(), miniMsg: MiniMessage = MiniMessage.miniMessage()) = miniMsg.deserialize(this, *tagResolver)

fun String.deser(vararg tagResolver: TagResolver = emptyArray()) = MiniMessage.miniMessage().deserialize(this, *tagResolver)

/* ******************************************************************** *
 *  Cross-platform util for conversion between Adventure and Minecraft  *
 * ******************************************************************** */

/**
 * Converts this adventure Component to a Minecraft Chat Component using the best platform specific conversion.
 */
fun Component.vanilla(): MutableComponent = ScafallProvider.get().adventure.toVanilla(this)

/* ************************************************************** *
 *  Util extension functions for creating simple text components  *
 * ************************************************************** */

fun Char.text(style: Style = Style.empty()) : TextComponent = Component.text(this, style)

fun Char.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()) : TextComponent = Component.text(this, textColor, *decorations)

fun String.text(style: Style = Style.empty()) : TextComponent = Component.text(this, style)

fun String.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()) : TextComponent = Component.text(this, textColor, *decorations)

fun Boolean.text(style: Style = Style.empty()) : TextComponent = Component.text(this, style)

fun Boolean.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()) : TextComponent = Component.text(this, textColor, *decorations)

fun Float.text(style: Style = Style.empty()) : TextComponent = Component.text(this, style)

fun Float.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()) : TextComponent = Component.text(this, textColor, *decorations)

fun Double.text(style: Style = Style.empty()) : TextComponent = Component.text(this, style)

fun Double.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()) : TextComponent = Component.text(this, textColor, *decorations)

fun Int.text(style: Style = Style.empty()) : TextComponent = Component.text(this, style)

fun Int.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()) : TextComponent = Component.text(this, textColor, *decorations)

fun Long.text(style: Style = Style.empty()) : TextComponent = Component.text(this, style)

fun Long.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()) : TextComponent = Component.text(this, textColor, *decorations)

operator fun Component.plus(component: Component) : Component {
    return this.append(component)
}

operator fun Component.plus(component: ComponentLike) : Component {
    return this.append(component)
}

operator fun Component.plus(component: ComponentBuilder<*, *>) : Component {
    return this.append(component)
}

/* ********************************************************************** *
 *  Util extension functions for creating placeholders and tag resolvers  *
 * ********************************************************************** */

fun String.parsed(value: String) : TagResolver.Single = Placeholder.parsed(this, value)

fun String.parsed(value: Any) : TagResolver.Single = Placeholder.parsed(this, value.toString())

fun String.unparsed(value: String) : TagResolver.Single = Placeholder.unparsed(this, value)

fun String.component(value: Component) : TagResolver.Single = Placeholder.component(this, value)
