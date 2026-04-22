package com.wolfyscript.scafall.compat

/**
 * Marks a field as a source of possible dependencies, that should be crawled when resolving dependencies.
 *
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class DependencySource(

    /**
     * Specifies if this field should be iterated when it is [Iterable].
     *
     * When enabled it iterates over the values (and their types) of the [Iterable] and crawls those to find dependencies.
     * If disabled and the annotated field is [Iterable], it treats the field as any other field.
     *
     * @return true if the resolver should iterate the values of this field; false when to treat this as a normal type
     */
    val flattenIterable: Boolean = true
)
