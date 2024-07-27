package com.wolfyscript.scaffolding

import com.wolfyscript.scaffolding.dependencies.DependencyHandler
import com.wolfyscript.scaffolding.dependencies.RepositoryHandler
import com.wolfyscript.scaffolding.factories.Factories
import com.wolfyscript.scaffolding.registry.Registries
import com.wolfyscript.scaffolding.scheduling.Scheduler
import org.reflections.Reflections

/**
 * The Entry to the Scaffolding API.
 *
 * It is usually registered in the service manager of the platform.
 */
interface Scaffolding {

    val registries: Registries

    val scheduler: Scheduler

    val reflections: Reflections

    val dependencyHandler: DependencyHandler

    val repositoryHandler: RepositoryHandler

    val factories: Factories

    fun createOrGetPluginWrapper(pluginName: String) : PluginWrapper



}