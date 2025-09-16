package com.wolfyscript.scafall.spigotlike.compat

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class PluginDependency(val pluginName: String, val id: String)
