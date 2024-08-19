package com.wolfyscript.scafall.dependency

import kotlin.reflect.full.createInstance

/**
 * Resolves the dependencies of a given type-value pair.
 * Usually used in conjunction with [DependencyResolverSettings]
 * @see DependencyResolverSettings
 */
interface DependencyResolver {
    /**
     * Resolves the dependencies for the specified type and value.
     * This creates a collection of all the dependencies this type and value depend on.
     *
     * @param value The value for which to get the dependencies
     * @param type  The type for which to get the dependencies
     * @return A collection of all dependencies of the given type & value
     */
    fun resolve(value: Any?, type: Class<*>?): Collection<Dependency>

    companion object {
        /**
         * Resolves the dependencies for the given type and value, plus all fields that may propagate their dependencies.
         *
         *
         * When the class of a field is annotated with [DependencyResolverSettings] then its dependencies are always included, otherwise they are ignored.<br></br>
         * To include the dependencies even when not directly available, annotate the field with [DependencySource],
         * this way this method will crawl the class & value of that field and look for further fields (recursive).
         *
         *
         * @param value The value to resolve
         * @param type  The type to resolve
         * @return A set of dependencies that this type and all included children depend on
         * @param <T> The type of the value
        </T> */
        fun <T> resolveDependenciesFor(value: T, type: Class<out T>): Set<Dependency> {
            val dependencies: MutableSet<Dependency> = HashSet()

            if (type.isAnnotationPresent(DependencyResolverSettings::class.java)) {
                val annotation = type.getAnnotation(
                    DependencyResolverSettings::class.java
                )
                try {
                    val resolver = annotation.value.createInstance()
                    dependencies.addAll(resolver.resolve(value, type)!!)
                } catch (e: ReflectiveOperationException) {
                    throw MissingDependencyException("Could not resolve dependency resolver settings", e)
                }
            }

            for (declaredField in type.declaredFields) {
                if (declaredField.trySetAccessible()) {
                    // Field type is directly annotated with resolver settings
                    if (declaredField.type.isAnnotationPresent(DependencyResolverSettings::class.java)) {
                        try {
                            val `object` = declaredField[value]
                            dependencies.addAll(resolveDependenciesFor(`object`, `object`.javaClass))
                        } catch (e: IllegalAccessException) {
                            throw MissingDependencyException(
                                "Failed to fetch dependencies of type '" + declaredField.type.name + "'!",
                                e
                            )
                        }
                    }

                    // Field type isn't providing dependencies directly, but may propagate dependencies
                    val dependencySource = declaredField.getAnnotation(
                        DependencySource::class.java
                    )
                    if (dependencySource != null) {
                        try {
                            val fieldObj = declaredField[value] ?: continue
                            if (dependencySource.flattenIterable && Iterable::class.java.isAssignableFrom(declaredField.type)) {
                                val iterable = fieldObj as Iterable<*>
                                for (element in iterable) {
                                    element?.let {
                                        dependencies.addAll(resolveDependenciesFor(it, it.javaClass))
                                    }
                                }
                            } else {
                                dependencies.addAll(resolveDependenciesFor(fieldObj, fieldObj.javaClass))
                            }
                        } catch (e: IllegalAccessException) {
                            throw MissingDependencyException("Failed to fetch dependencies of type '" + declaredField.type.name + "'!", e)
                        }
                    }
                }
            }

            val superType = type.superclass
            if (superType != null) {
                dependencies.addAll(resolveDependenciesFor(value, superType))
            }

            return dependencies
        }
    }
}
