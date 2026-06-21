package com.wolfyscript.scafall.maven

import com.wolfyscript.scafall.identifier.Key
import java.util.Collections.unmodifiableSet

class MavenRepositoryHandlerImpl() : MavenRepositoryHandler {

    private val internalRepositories: MutableSet<MavenRepository> = mutableSetOf()
    override val repositories: Set<MavenRepository>
        get() = unmodifiableSet(internalRepositories)

    private val internalPluginRepositories: MutableMap<Key, Set<MavenRepository>> = mutableMapOf()

    override fun pluginRepositories(plugin: Key): Set<MavenRepository> {
        return internalPluginRepositories.getOrDefault(plugin, setOf())
    }

}