package com.wolfyscript.scafall

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
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

}

fun Key.toAPI() : com.wolfyscript.scafall.identifier.Key = ScafallProvider.get().factories.identifierFactory.key(this.namespace(), this.value())

/* ****************************************** *
 *  Util extension functions for MiniMessage  *
 * ****************************************** */

fun String.deserialize(miniMsg: MiniMessage = MiniMessage.miniMessage(), tagResolver: TagResolver = TagResolver.empty()) = miniMsg.deserialize(this, tagResolver)

fun String.deserialize(miniMsg: MiniMessage = MiniMessage.miniMessage(), vararg tagResolver: TagResolver = emptyArray()) = miniMsg.deserialize(this, *tagResolver)

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
