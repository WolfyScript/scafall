package com.wolfyscript.scafall.config.jackson

import kotlin.reflect.KClass

/**
 * Annotation used to define a base type for keyed serialization in the Jackson configuration.
 *
 * This annotation is used to specify the base type that should be used for serialization
 * and deserialization when dealing with keyed objects. It allows the system to properly
 * identify and handle different implementations of the same base type based on their keys.
 *
 * @property baseType The [KClass] representing the base type for keyed serialization.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class KeyedBaseType(val baseType: KClass<*>)
