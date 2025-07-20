package com.wolfyscript.scafall.maven

import com.wolfyscript.scafall.ModWrapper

interface MavenRepositoryHandler {

    val repositories: Set<MavenRepository>

    fun pluginRepositories(plugin: ModWrapper): Set<MavenRepository>
}