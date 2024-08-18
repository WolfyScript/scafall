package com.wolfyscript.scaffolding.spigot.platform.compatibility

import com.wolfyscript.scaffolding.dependency.Dependency
import java.util.*

class PluginIntegrationDependency(val pluginIntegration: PluginIntegration) :
    Dependency {
    override val isAvailable: Boolean
        get() = pluginIntegration.isDoneLoading

    override fun toString(): String {
        return pluginIntegration.associatedPlugin
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PluginIntegrationDependency
        return pluginIntegration == that.pluginIntegration
    }

    override fun hashCode(): Int {
        return Objects.hashCode(pluginIntegration)
    }
}
