package com.wolfyscript.scafall.identifier

/**
 * Annotation class for creating static namespaced keys used for the annotated type.
 *
 * This annotation allows defining a key either by providing a complete key string [value],
 * or by specifying a [namespace] and [key] separately.
 *
 * The [KeyBuilder] inner object provides utility methods for constructing key strings
 * based on the annotation's properties.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class StaticNamespacedKey(
    val value: String = "",
    /**
     * @return The namespace of the key
     */
    val namespace: String = Key.SCAFFOLDING_NAMESPACE,
    /**
     *
     * @return The key to the object
     */
    val key: String = ""
) {
    object KeyBuilder {
        fun createKeyString(annotated: Class<*>): String {
            val annotation = annotated.getAnnotation(StaticNamespacedKey::class.java)
            if (annotation != null) {
                if (annotation.value.isNotBlank()) {
                    return annotation.value
                }
                if (annotation.namespace.isNotBlank() && annotation.key.isNotBlank()) {
                    return annotation.namespace + ":" + annotation.key
                }
            }
            throw IllegalArgumentException("Invalid static id properties! Either use the value, or both the namespace and key options!")
        }
    }
}
