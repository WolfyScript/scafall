package com.wolfyscript.scaffolding.spigot.platform.compatibility

import com.wolfyscript.scaffolding.ScaffoldingProvider.Companion.get
import com.wolfyscript.scaffolding.dependency.Dependency
import com.wolfyscript.scaffolding.dependency.DependencyResolver
import com.wolfyscript.scaffolding.dependency.MissingDependencyException
import com.wolfyscript.scaffolding.spigot.api.compatibilityManager

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
