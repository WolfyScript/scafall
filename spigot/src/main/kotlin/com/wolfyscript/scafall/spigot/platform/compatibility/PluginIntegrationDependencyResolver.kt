package com.wolfyscript.scafall.spigot.platform.compatibility

import com.wolfyscript.scafall.ScafallProvider.Companion.get
import com.wolfyscript.scafall.dependency.Dependency
import com.wolfyscript.scafall.dependency.DependencyResolver
import com.wolfyscript.scafall.dependency.MissingDependencyException
import com.wolfyscript.scafall.spigot.api.compatibilityManager

class PluginIntegrationDependencyResolver : DependencyResolver {

    override fun resolve(value: Any?, type: Class<*>?): Collection<Dependency> {
        val pluginsCompat = get().compatibilityManager.plugins

        if (type?.isAnnotationPresent(PluginIntegrationDependencyResolverSettings::class.java) == true) {
            val annotation = type.getAnnotation(PluginIntegrationDependencyResolverSettings::class.java)
            val integration = pluginsCompat.getIntegration(annotation.pluginName, annotation.integration.java)
                ?: throw MissingDependencyException("Declared integration dependency '" + annotation.pluginName + "' not found for type '" + type.name + "'!")
            return setOf(PluginIntegrationDependency(integration))
        }

        return setOf()
    }
}
