package com.wolfyscript.scaffolding.dependency

import kotlin.reflect.KClass

/**
 * Used to specify the [DependencyResolver] for the annotated type.
 *
 *
 * This is used for classes & values that may depend on something on their own.
 * Dependencies are resolved by crawling over the tree of classes & values, that are annotated with [DependencyResolverSettings] and [DependencySource].
 *
 *
 *
 * This annotation is only necessary when the annotated type (or its instances) provide a list of their own dependencies.
 * If you are looking for propagating dependencies from the fields use [DependencySource] to annotate the fields instead!
 *
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
