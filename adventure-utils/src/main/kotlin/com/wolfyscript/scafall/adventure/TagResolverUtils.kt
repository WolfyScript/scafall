package com.wolfyscript.scafall.adventure

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

/**
 * Creates a parsed placeholder tag resolver for the given value.
 *
 * A parsed placeholder will have its content processed by the MiniMessage parser,
 * allowing for nested formatting and other MiniMessage features.
 *
 * @param value The value to create a placeholder for
 * @return A [TagResolver.Single] that can be used in MiniMessage formatting
 */
fun String.parsed(value: String): TagResolver.Single = Placeholder.parsed(this, value)

/**
 * Creates a parsed placeholder tag resolver for the given value.
 *
 * A parsed placeholder will have its content processed by the MiniMessage parser,
 * allowing for nested formatting and other MiniMessage features.
 *
 * @param value The value to create a placeholder for
 * @return A [TagResolver.Single] that can be used in MiniMessage formatting
 */
fun String.parsed(value: Any): TagResolver.Single = Placeholder.parsed(this, value.toString())

/**
 * Creates an unparsed placeholder tag resolver for the given value.
 *
 * An unparsed placeholder will have its content treated as literal text without
 * MiniMessage processing, making it suitable for content that should not be formatted.
 *
 * @param value The value to create a placeholder for
 * @return A [TagResolver.Single] that can be used in MiniMessage formatting
 */
fun String.unparsed(value: String): TagResolver.Single = Placeholder.unparsed(this, value)

/**
 * Creates a component placeholder tag resolver for the given value.
 *
 * This allows for pre-formatted Component objects to be inserted directly
 * into MiniMessage content without additional processing.
 *
 * @param value The Component to create a placeholder for
 * @return A [TagResolver.Single] that can be used in MiniMessage formatting
 */
fun String.component(value: Component): TagResolver.Single = Placeholder.component(this, value)
