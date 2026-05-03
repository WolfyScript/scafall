package com.wolfyscript.scafall.config.jackson

/**
 * Annotation to specify the default namespace for a class.
 *
 * This annotation can be used to define a default namespace that will be
 * applied when serializing/deserializing objects of the annotated class.
 * The namespace is used to prefix keys or identifiers in configuration files.
 *
 * @property namespace the default namespace to be used for the annotated class
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class DefaultNamespace(val namespace: String)
