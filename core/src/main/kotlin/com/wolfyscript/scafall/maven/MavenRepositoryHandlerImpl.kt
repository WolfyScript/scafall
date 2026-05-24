package com.wolfyscript.scafall.maven

import com.wolfyscript.scafall.core.ModIdentifier
import java.util.Collections.unmodifiableSet

class MavenRepositoryHandlerImpl() : MavenRepositoryHandler {

    private val internalRepositories: MutableSet<MavenRepository> = mutableSetOf()
    override val repositories: Set<MavenRepository>
        get() = unmodifiableSet(internalRepositories)

    private val internalPluginRepositories: MutableMap<ModIdentifier, Set<MavenRepository>> = mutableMapOf()

    override fun pluginRepositories(plugin: ModIdentifier): Set<MavenRepository> {
        return internalPluginRepositories.getOrDefault(plugin, setOf())
    }

}