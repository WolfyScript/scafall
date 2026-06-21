package com.wolfyscript.scafall.maven

import com.wolfyscript.scafall.identifier.Key

interface MavenRepositoryHandler {

    val repositories: Set<MavenRepository>

    fun pluginRepositories(plugin: Key): Set<MavenRepository>
}