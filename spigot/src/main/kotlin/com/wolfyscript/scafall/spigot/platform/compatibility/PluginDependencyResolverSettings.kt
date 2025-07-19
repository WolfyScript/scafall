package com.wolfyscript.scafall.spigot.platform.compatibility

import com.wolfyscript.scafall.compat.Dependency
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class PluginDependencyResolverSettings(
    val dependencyType: KClass<out Dependency>
)
