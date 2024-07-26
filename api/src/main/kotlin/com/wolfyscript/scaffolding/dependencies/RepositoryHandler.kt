package com.wolfyscript.scaffolding.dependencies

import com.wolfyscript.scaffolding.PluginWrapper

interface RepositoryHandler {

    val repositories: Set<Repository>

    fun pluginRepositories(plugin: PluginWrapper): Set<Repository>
}