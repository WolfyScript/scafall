package com.wolfyscript.scafall.spigot.compat

import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.ScafallSpigot
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginEnableEvent

class PluginDependencyLoader(val scafall: ScafallSpigot) : Listener {

    fun loadDependencies() {
        val annotated = scafall.reflections.getTypesAnnotatedWith(PluginDependency::class.java)
        for (type in annotated) {
            val annotation = type.getAnnotation(PluginDependency::class.java)
            if (annotation != null && Dependency::class.java.isAssignableFrom(type)) {
                if (Bukkit.getPluginManager().isPluginEnabled(annotation.pluginName)) {
                    val dependency = Dependency::class.java.cast(type.getConstructor().newInstance())
                    val key = if (annotation.id.contains(":")) {
                        Key.parse(annotation.id)
                    } else {
                        Key.defaultKey(annotation.id)
                    }
                    scafall.dependencyManager.loadDependency(key, dependency)
                }
            }
        }
    }

    @EventHandler
    private fun onPluginEnable(event: PluginEnableEvent) {
        loadDependencies()
    }

}
