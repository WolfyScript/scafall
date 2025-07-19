package com.wolfyscript.scafall.spigot.compat

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class PluginDependency(val pluginName: String, val id: String)
