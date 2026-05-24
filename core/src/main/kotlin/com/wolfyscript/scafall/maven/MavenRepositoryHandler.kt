package com.wolfyscript.scafall.maven

import com.wolfyscript.scafall.core.ModIdentifier

interface MavenRepositoryHandler {

    val repositories: Set<MavenRepository>

    fun pluginRepositories(plugin: ModIdentifier): Set<MavenRepository>
}