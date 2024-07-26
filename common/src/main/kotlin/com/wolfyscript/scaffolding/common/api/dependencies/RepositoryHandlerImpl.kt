package com.wolfyscript.scaffolding.common.api.dependencies

import com.wolfyscript.scaffolding.PluginWrapper
import com.wolfyscript.scaffolding.dependencies.Repository
import com.wolfyscript.scaffolding.dependencies.RepositoryHandler
import java.util.Collections.unmodifiableSet

class RepositoryHandlerImpl() : RepositoryHandler {

    private val internalRepositories: MutableSet<Repository> = mutableSetOf()
    override val repositories: Set<Repository>
        get() = unmodifiableSet(internalRepositories)

    private val internalPluginRepositories: MutableMap<PluginWrapper, Set<Repository>> = mutableMapOf()

    override fun pluginRepositories(plugin: PluginWrapper): Set<Repository> {
        return internalPluginRepositories.getOrDefault(plugin, setOf())
    }

}