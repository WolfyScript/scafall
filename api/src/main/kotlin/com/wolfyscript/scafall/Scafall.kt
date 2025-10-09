package com.wolfyscript.scafall

import com.wolfyscript.scafall.compat.DependencyManager
import com.wolfyscript.scafall.config.jackson.JacksonUtil
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.loader.module.Client
import com.wolfyscript.scafall.loader.module.Module
import com.wolfyscript.scafall.platform.PlatformManager
import com.wolfyscript.scafall.registry.ScafallRegistries
import com.wolfyscript.scafall.scheduling.Scheduler
import com.wolfyscript.scafall.server.ScafallServer
import com.wolfyscript.scafall.wrappers.MinecraftWrapper
import org.slf4j.Logger

/**
 * The Entry to the Scaffolding API.
 *
 * It is usually registered in the service manager of the platform.
 */
interface Scafall : Module<ScafallServer, Client>{

    /**
     * All the provided registries that scafall provides
     */
    val registries: ScafallRegistries

    val dependencyManager: DependencyManager

    val scheduler: Scheduler

    val platformManager: PlatformManager

    val mavenDependencyHandler: MavenDependencyHandler

    val mavenRepositoryHandler: MavenRepositoryHandler

    val factories: Factories

    val modInfo: ModWrapper

    val minecraftWrapper: MinecraftWrapper

    val logger: Logger
        get() = modInfo.logger

    val jacksonUtil: JacksonUtil

    fun createOrGetPluginWrapper(pluginName: String) : ModWrapper?

    companion object

}