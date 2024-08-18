package com.wolfyscript.scaffolding.maven

import com.wolfyscript.scaffolding.PluginWrapper

interface MavenRepositoryHandler {

    val repositories: Set<MavenRepository>

    fun pluginRepositories(plugin: PluginWrapper): Set<MavenRepository>
}