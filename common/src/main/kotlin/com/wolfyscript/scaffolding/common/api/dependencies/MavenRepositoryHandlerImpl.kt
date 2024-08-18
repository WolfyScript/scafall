package com.wolfyscript.scaffolding.common.api.dependencies

import com.wolfyscript.scaffolding.PluginWrapper
import com.wolfyscript.scaffolding.maven.MavenRepository
import com.wolfyscript.scaffolding.maven.MavenRepositoryHandler
import java.util.Collections.unmodifiableSet

class MavenRepositoryHandlerImpl() : MavenRepositoryHandler {

    private val internalRepositories: MutableSet<MavenRepository> = mutableSetOf()
    override val repositories: Set<MavenRepository>
        get() = unmodifiableSet(internalRepositories)

    private val internalPluginRepositories: MutableMap<PluginWrapper, Set<MavenRepository>> = mutableMapOf()

    override fun pluginRepositories(plugin: PluginWrapper): Set<MavenRepository> {
        return internalPluginRepositories.getOrDefault(plugin, setOf())
    }

}