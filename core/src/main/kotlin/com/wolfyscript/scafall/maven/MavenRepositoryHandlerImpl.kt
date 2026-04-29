package com.wolfyscript.scafall.maven

import com.wolfyscript.scafall.ModWrapper
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