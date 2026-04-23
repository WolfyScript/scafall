package com.wolfyscript.scafall.dependencies

import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.maven.MavenRepository
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import java.util.Collections.unmodifiableSet

class MavenRepositoryHandlerImpl() : MavenRepositoryHandler {

    private val internalRepositories: MutableSet<MavenRepository> = mutableSetOf()
    override val repositories: Set<MavenRepository>
        get() = unmodifiableSet(internalRepositories)

    private val internalPluginRepositories: MutableMap<ModWrapper, Set<MavenRepository>> = mutableMapOf()

    override fun pluginRepositories(plugin: ModWrapper): Set<MavenRepository> {
        return internalPluginRepositories.getOrDefault(plugin, setOf())
    }

}