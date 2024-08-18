package com.wolfyscript.scaffolding.spigot.platform.compatibility

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class PluginIntegrationDependencyResolverSettings(
    val pluginName: String,
    val integration: KClass<out PluginIntegration>
)
