package com.wolfyscript.scaffolding

import com.wolfyscript.scaffolding.maven.MavenDependencyHandler
import com.wolfyscript.scaffolding.maven.MavenRepositoryHandler
import com.wolfyscript.scaffolding.factories.Factories
import com.wolfyscript.scaffolding.platform.PlatformType
import com.wolfyscript.scaffolding.registry.Registries
import com.wolfyscript.scaffolding.scheduling.Scheduler
import org.reflections.Reflections
import org.slf4j.Logger

/**
 * The Entry to the Scaffolding API.
 *
 * It is usually registered in the service manager of the platform.
 */
interface Scaffolding {

    val registries: Registries

    val scheduler: Scheduler

    val platformType: PlatformType

    val reflections: Reflections

    val mavenDependencyHandler: MavenDependencyHandler

    val mavenRepositoryHandler: MavenRepositoryHandler

    val factories: Factories

    val corePlugin: PluginWrapper

    val adventure: AdventureUtil

    val logger: Logger
        get() = corePlugin.logger

    fun createOrGetPluginWrapper(pluginName: String) : PluginWrapper

    companion object

}