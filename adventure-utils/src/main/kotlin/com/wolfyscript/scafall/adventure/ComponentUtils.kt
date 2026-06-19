package com.wolfyscript.scafall.adventure

import com.google.gson.JsonParseException
import com.mojang.serialization.JsonOps
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentBuilder
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.ComponentSerialization

/**
 * Converts this adventure Component to a Minecraft Chat Component using the best platform specific conversion.
 *
 * @return The vanilla Minecraft chat component representation of this adventure component
 */
fun Component.vanilla(): net.minecraft.network.chat.Component {
    val json = GsonComponentSerializer.gson().serializeToTree(this)
    val holder = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY)

    val result = ComponentSerialization.CODEC.decode(holder.createSerializationContext(JsonOps.INSTANCE), json)
    if (result.isError) {
        throw JsonParseException(result.error().get().message())
    }
    return result.result().get().first
}

/**
 * Creates a TextComponent from a character with optional style.
 *
 * @param style The style to apply to the text component
 * @return A TextComponent containing the character
 */
fun Char.text(style: Style = Style.empty()): TextComponent = Component.text(this, style)

/**
 * Creates a TextComponent from a character with optional text color and decorations.
 *
 * @param textColor The color to apply to the text (optional)
 * @param decorations The text decorations to apply (optional)
 * @return A TextComponent containing the character with specified formatting
 */
fun Char.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()): TextComponent =
    Component.text(this, textColor, *decorations)

/**
 * Creates a TextComponent from a string with optional style.
 *
 * @param style The style to apply to the text component
 * @return A TextComponent containing the string
 */
fun String.text(style: Style = Style.empty()): TextComponent = Component.text(this, style)

/**
 * Creates a TextComponent from a string with optional text color and decorations.
 *
 * @param textColor The color to apply to the text (optional)
 * @param decorations The text decorations to apply (optional)
 * @return A TextComponent containing the string with specified formatting
 */
fun String.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()): TextComponent =
    Component.text(this, textColor, *decorations)

/**
 * Creates a TextComponent from a boolean with optional style.
 *
 * @param style The style to apply to the text component
 * @return A TextComponent containing the boolean value
 */
fun Boolean.text(style: Style = Style.empty()): TextComponent = Component.text(this, style)

/**
 * Creates a TextComponent from a boolean with optional text color and decorations.
 *
 * @param textColor The color to apply to the text (optional)
 * @param decorations The text decorations to apply (optional)
 * @return A TextComponent containing the boolean value with specified formatting
 */
fun Boolean.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()): TextComponent =
    Component.text(this, textColor, *decorations)

/**
 * Creates a TextComponent from a float with optional style.
 *
 * @param style The style to apply to the text component
 * @return A TextComponent containing the float value
 */
fun Float.text(style: Style = Style.empty()): TextComponent = Component.text(this, style)

/**
 * Creates a TextComponent from a float with optional text color and decorations.
 *
 * @param textColor The color to apply to the text (optional)
 * @param decorations The text decorations to apply (optional)
 * @return A TextComponent containing the float value with specified formatting
 */
fun Float.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()): TextComponent =
    Component.text(this, textColor, *decorations)

/**
 * Creates a TextComponent from a double with optional style.
 *
 * @param style The style to apply to the text component
 * @return A TextComponent containing the double value
 */
fun Double.text(style: Style = Style.empty()): TextComponent = Component.text(this, style)

/**
 * Creates a TextComponent from a double with optional text color and decorations.
 *
 * @param textColor The color to apply to the text (optional)
 * @param decorations The text decorations to apply (optional)
 * @return A TextComponent containing the double value with specified formatting
 */
fun Double.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()): TextComponent =
    Component.text(this, textColor, *decorations)

/**
 * Creates a TextComponent from an integer with optional style.
 *
 * @param style The style to apply to the text component
 * @return A TextComponent containing the integer value
 */
fun Int.text(style: Style = Style.empty()): TextComponent = Component.text(this, style)

/**
 * Creates a TextComponent from an integer with optional text color and decorations.
 *
 * @param textColor The color to apply to the text (optional)
 * @param decorations The text decorations to apply (optional)
 * @return A TextComponent containing the integer value with specified formatting
 */
fun Int.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()): TextComponent =
    Component.text(this, textColor, *decorations)

/**
 * Creates a TextComponent from a long with optional style.
 *
 * @param style The style to apply to the text component
 * @return A TextComponent containing the long value
 */
fun Long.text(style: Style = Style.empty()): TextComponent = Component.text(this, style)

/**
 * Creates a TextComponent from a long with optional text color and decorations.
 *
 * @param textColor The color to apply to the text (optional)
 * @param decorations The text decorations to apply (optional)
 * @return A TextComponent containing the long value with specified formatting
 */
fun Long.text(textColor: TextColor? = null, vararg decorations: TextDecoration = emptyArray()): TextComponent =
    Component.text(this, textColor, *decorations)

/**
 * Concatenates this Component with another Component.
 *
 * @param component The Component to append to this Component
 * @return A new Component that is the result of appending the other Component
 */
operator fun Component.plus(component: Component): Component {
    return this.append(component)
}

/**
 * Concatenates this Component with a ComponentLike.
 *
 * @param component The ComponentLike to append to this Component
 * @return A new Component that is the result of appending the other ComponentLike
 */
operator fun Component.plus(component: ComponentLike): Component {
    return this.append(component)
}

/**
 * Concatenates this Component with a ComponentBuilder.
 *
 * @param component The ComponentBuilder to append to this Component
 * @return A new Component that is the result of appending the other ComponentBuilder
 */
operator fun Component.plus(component: ComponentBuilder<*, *>): Component {
    return this.append(component)
}
