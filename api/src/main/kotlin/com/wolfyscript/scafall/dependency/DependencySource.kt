package com.wolfyscript.scafall.dependency

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
     *
     * When true it iterates over the values and crawls each value (& its type).<br></br>
     * When false it treats the field and crawls the types fields instead.
     *
     *
     * @return true if the resolver should iterate the values of this field; false when to treat this as a normal type
     */
    val flattenIterable: Boolean = true
)
