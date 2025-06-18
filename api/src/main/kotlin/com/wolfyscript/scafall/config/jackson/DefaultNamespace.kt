package com.wolfyscript.scafall.config.jackson

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class DefaultNamespace(val namespace: String)
