package com.wolfyscript.scafall

import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.registry.Registries
import com.wolfyscript.scafall.scheduling.Scheduler
import org.reflections.Reflections
import org.slf4j.Logger

/**
 * The Entry to the Scaffolding API.
 *
 * It is usually registered in the service manager of the platform.
 */
interface Scafall {

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