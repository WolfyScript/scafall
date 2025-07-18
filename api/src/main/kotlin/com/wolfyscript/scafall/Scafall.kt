package com.wolfyscript.scafall

import com.wolfyscript.scafall.adventure.AdventureUtil
import com.wolfyscript.scafall.compat.DependencyManager
import com.wolfyscript.scafall.config.jackson.JacksonUtil
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.platform.PlatformManager
import com.wolfyscript.scafall.registry.ScafallRegistries
import com.wolfyscript.scafall.scheduling.Scheduler
import com.wolfyscript.scafall.server.ScafallServer
import com.wolfyscript.scafall.wrappers.utils.MinecraftWrapper
import org.reflections.Reflections
import org.slf4j.Logger

/**
 * The Entry to the Scaffolding API.
 *
 * It is usually registered in the service manager of the platform.
 */
interface Scafall {

    /**
     * All the provided registries that scafall provides
     */
    val registries: ScafallRegistries

    val dependencyManager: DependencyManager

    val scheduler: Scheduler

    val platformManager: PlatformManager

    val reflections: Reflections

    val mavenDependencyHandler: MavenDependencyHandler

    val mavenRepositoryHandler: MavenRepositoryHandler

    val factories: Factories

    val corePlugin: PluginWrapper

    val server: ScafallServer

    val adventure: AdventureUtil

    val minecraftWrapper: MinecraftWrapper

    val logger: Logger
        get() = corePlugin.logger

    val jacksonUtil: JacksonUtil

    fun createOrGetPluginWrapper(pluginName: String) : PluginWrapper?

    companion object

}