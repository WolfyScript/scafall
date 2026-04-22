package com.wolfyscript.scafall.config.jackson

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class NamespacedBaseType(val baseType: KClass<*>)
