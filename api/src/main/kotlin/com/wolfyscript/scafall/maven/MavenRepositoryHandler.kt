package com.wolfyscript.scafall.maven

import com.wolfyscript.scafall.PluginWrapper

interface MavenRepositoryHandler {

    val repositories: Set<MavenRepository>

    fun pluginRepositories(plugin: PluginWrapper): Set<MavenRepository>
}