package com.wolfyscript.scafall.spigot.compat

import com.wolfyscript.scafall.compat.Dependency
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class PluginDependencyResolverSettings(
    val dependencyType: KClass<out Dependency>
)
