package com.wolfyscript.scaffolding.spigot.api

import com.wolfyscript.scaffolding.PluginWrapper
import com.wolfyscript.scaffolding.common.api.AbstractScaffoldingImpl
import com.wolfyscript.scaffolding.common.api.dependencies.DependencyHandlerImpl
import com.wolfyscript.scaffolding.common.api.dependencies.RepositoryHandlerImpl
import com.wolfyscript.scaffolding.common.api.factories.CommonFactories
import com.wolfyscript.scaffolding.common.api.registries.CommonRegistries
import com.wolfyscript.scaffolding.dependencies.DependencyHandler
import com.wolfyscript.scaffolding.dependencies.RepositoryHandler
import com.wolfyscript.scaffolding.factories.Factories
import com.wolfyscript.scaffolding.registry.Registries
import com.wolfyscript.scaffolding.scheduling.Scheduler
import com.wolfyscript.scaffolding.spigot.ScaffoldingSpigotBootstrap
import com.wolfyscript.scaffolding.spigot.api.scheduling.SchedulerImpl

internal class ScaffoldingSpigot(bootstrap: ScaffoldingSpigotBootstrap) : AbstractScaffoldingImpl() {

    override val registries: Registries = CommonRegistries()
    override val scheduler: Scheduler = SchedulerImpl(this)
    override val dependencyHandler: DependencyHandler = DependencyHandlerImpl(this, bootstrap.corePlugin.plugin.dataFolder.toPath().resolve("libs"))
    override val repositoryHandler: RepositoryHandler = RepositoryHandlerImpl()
    override val factories: Factories = CommonFactories()

    override fun createOrGetPluginWrapper(pluginName: String): PluginWrapper {
        TODO("Not yet implemented")
    }

}