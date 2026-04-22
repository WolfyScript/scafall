package com.wolfyscript.scafall.i18n

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

interface I18n {

    /**
     * Creates a [Component] of the specified language key.<br></br>
     * If the key exists in the language it will be translated and returns the according component.
     * If it is not available it returns an empty component.
     *
     * @param key The key in the language.
     * @return The component set for the key; empty component if not available.
     */
    fun translated(key: String): Component

    /**
     * Creates a [Component] of the specified language key.<br></br>
     * If the key exists in the language it will be translated and returns the according component.
     * If it is not available it returns an empty component.
     *
     * @param key The key in the language.
     * @param resolvers The custom tag resolvers to use.
     * @return The component set for the key; empty component if not available.
     */
    fun translated(key: String, vararg resolvers: TagResolver): Component

    /**
     * Creates a [Component] of the specified language key.<br></br>
     * If the key exists in the language it will be translated and returns the according component.
     * If it is not available it returns an empty component.
     *
     * @param key The key in the language.
     * @param resolver The custom tag resolver to use.
     * @return The component set for the key; empty component if not available.
     */
    fun translated(key: String, resolver: TagResolver): Component

}