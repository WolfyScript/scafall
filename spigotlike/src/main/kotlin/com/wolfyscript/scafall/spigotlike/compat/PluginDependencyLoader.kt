package com.wolfyscript.scafall.spigotlike.compat

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerLoadEvent

class PluginDependencyLoader(val scafall: Scafall) : Listener {

    fun loadDependencies() {
        scafall.logger.info("Detecting dependencies...")
        val dependencies = ScafallRegistryTypes.dependencies.resolveOrThrow()
        for (depType in dependencies.values()) {
            val annotation = depType.getAnnotation(PluginDependency::class.java)
            if (annotation != null && Dependency::class.java.isAssignableFrom(depType)) {
                val key = if (annotation.id.contains(":")) {
                    Key.parse(annotation.id)
                } else {
                    Key.defaultKey(annotation.id)
                }

                if (scafall.dependencyManager.getDependency(key) != null) {
                    continue
                }

                if (Bukkit.getPluginManager().getPlugin(annotation.pluginName) != null) {
                    try {
                        val dependency = Dependency::class.java.cast(depType.getConstructor().newInstance())
                        scafall.dependencyManager.loadDependency(key, dependency)
                        scafall.logger.info("   ${annotation.pluginName}: loaded")
                    } catch (e: Exception) { // Catch all the types of exception since we don't necessarily control the dependency classes
                        scafall.logger.error("   ${annotation.pluginName}: failed", e)
                    }
                }
            }
        }
    }

    @EventHandler
    private fun onServerCompleteLoad(event: ServerLoadEvent) {
        if (event.type == ServerLoadEvent.LoadType.STARTUP) {
            //
            // A dependency associated with a plugin, that failed to enable during startup, cannot be initialized,
            // so we mark it as failed at this point already.
            //
            // A plugin can be enabled at this point but fail to initialize later.
            // That case is handled by a separate time out.
            //
            val dependencies = ScafallRegistryTypes.dependencies.resolveOrThrow()
            for (depType in dependencies.values()) {
                val annotation = depType.getAnnotation(PluginDependency::class.java)
                if (annotation != null && Dependency::class.java.isAssignableFrom(depType)) {
                    val key = if (annotation.id.contains(":")) {
                        Key.parse(annotation.id)
                    } else {
                        Key.defaultKey(annotation.id)
                    }
                    if (scafall.dependencyManager.getDependency(key) == null) {
                        continue
                    }
                    if (!Bukkit.getPluginManager().isPluginEnabled(annotation.pluginName)) {
                        scafall.logger.warn("   ${annotation.pluginName}: failed (ignore)")
                        scafall.dependencyManager.failedToInitDependency(key)
                    }
                }
            }
        }
    }

}
