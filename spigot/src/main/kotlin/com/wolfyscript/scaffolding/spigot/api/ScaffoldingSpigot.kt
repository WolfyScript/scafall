package com.wolfyscript.scaffolding.spigot.api

import com.wolfyscript.scaffolding.common.api.AbstractScaffoldingImpl
import com.wolfyscript.scaffolding.dependencies.DependencyHandler
import com.wolfyscript.scaffolding.dependencies.RepositoryHandler
import com.wolfyscript.scaffolding.registry.Registries
import com.wolfyscript.scaffolding.scheduling.Scheduler
import com.wolfyscript.scaffolding.spigot.ScaffoldingSpigotBootstrap

internal class ScaffoldingSpigot(internal val bootstrap: ScaffoldingSpigotBootstrap) : AbstractScaffoldingImpl() {

    override val registries: Registries = TODO()
    override val scheduler: Scheduler = TODO()
    override val dependencyHandler: DependencyHandler = TODO()
    override val repositoryHandler: RepositoryHandler = TODO()

}