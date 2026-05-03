package com.wolfyscript.scafall.adventure

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

fun String.parsed(value: String): TagResolver.Single = Placeholder.parsed(this, value)

fun String.parsed(value: Any): TagResolver.Single = Placeholder.parsed(this, value.toString())

fun String.unparsed(value: String): TagResolver.Single = Placeholder.unparsed(this, value)

fun String.component(value: Component): TagResolver.Single = Placeholder.component(this, value)
