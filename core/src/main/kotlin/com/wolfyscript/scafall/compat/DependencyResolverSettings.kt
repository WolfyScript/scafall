package com.wolfyscript.scafall.compat

import kotlin.reflect.KClass

/**
 * Used to specify the [DependencyResolver] for the annotated type.
 *
 * This is used for classes and values that depend on something and will return a list of dependencies.
 *
 * This annotation is only necessary when the annotated type or its instances provide a list of their own custom dependencies.
 * If you are looking for propagating dependencies from the fields use [DependencySource] to annotate the fields instead!
 *
 * For example, a [com.wolfyscript.scafall.items.ItemStackRef] implementation may depend on another plugin/mod, which it will resolve via the annotated [DependencyResolver] and return via a list.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class DependencyResolverSettings(
    /**
     * Specifies the type of the [DependencyResolver]
     *
     * @return The type of the [DependencyResolver] to use
     */
    val value: KClass<out DependencyResolver>
)
