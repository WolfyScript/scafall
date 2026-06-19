package com.wolfyscript.scafall.adventure

import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

/**
 * Deserialize a string using the provided tag resolver and MiniMessage instance.
 *
 * This extension function allows for deserializing a string into an Adventure Component
 * using a specific TagResolver and MiniMessage instance.
 *
 * @param tagResolver The tag resolver to use for parsing placeholders in the string
 * @param miniMsg The MiniMessage instance to use for deserialization
 * @return The deserialized Component
 */
fun String.deser(tagResolver: TagResolver = TagResolver.empty(), miniMsg: MiniMessage = MiniMessage.miniMessage()) =
    miniMsg.deserialize(this, tagResolver)

/**
 * Deserialize a string using the provided tag resolvers and MiniMessage instance.
 *
 * This extension function allows for deserializing a string into an Adventure Component
 * using multiple TagResolvers and a specific MiniMessage instance.
 *
 * @param tagResolver The tag resolvers to use for parsing placeholders in the string
 * @param miniMsg The MiniMessage instance to use for deserialization
 * @return The deserialized Component
 */
fun String.deser(vararg tagResolver: TagResolver = emptyArray(), miniMsg: MiniMessage = MiniMessage.miniMessage()) =
    miniMsg.deserialize(this, *tagResolver)

/**
 * Deserialize a string using the provided tag resolvers.
 *
 * This extension function allows for deserializing a string into an Adventure Component
 * using multiple TagResolvers with the default MiniMessage instance.
 *
 * @param tagResolver The tag resolvers to use for parsing placeholders in the string
 * @return The deserialized Component
 */
fun String.deser(vararg tagResolver: TagResolver = emptyArray()) =
    MiniMessage.miniMessage().deserialize(this, *tagResolver)
