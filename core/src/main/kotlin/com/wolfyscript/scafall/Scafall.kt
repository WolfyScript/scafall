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
import com.wolfyscript.scafall.scheduler.Scheduler
import com.wolfyscript.scafall.server.ScafallServer
import org.slf4j.Logger

/**
 * The Entry to the Scaffolding API.
 *
 * It is usually registered in the service manager of the platform.
 */
interface Scafall : Module<ScafallServer, Client> {

    /**
     * Provides access to all the registries that Scafall offers.
     * These registries can be used to register and manage various components of the Scafall API.
     */
    val registries: ScafallRegistries

    /**
     * Manages dependencies of third-party libraries.
     * This includes both internal and external dependencies, allowing for easy resolution and management.
     */
    val dependencyManager: DependencyManager

    /**
     * Manages the scheduling of tasks independent of the server platform.
     * This scheduler can be used to run tasks at specific intervals and ticks.
     */
    val scheduler: Scheduler

    /**
     * Manages the platform on which the Scafall API is running.
     */
    val platformManager: PlatformManager

    /**
     * Manages Maven dependencies.
     */
    val mavenDependencyHandler: MavenDependencyHandler

    /**
     * Manages Maven repositories.
     */
    val mavenRepositoryHandler: MavenRepositoryHandler

    /**
     * Provides access to various factories used within the Scafall API.
     * These factories can be used to create instances of various components of the Scafall API.
     */
    val factories: Factories

    /**
     * Provides access to the mod information for the Scafall API.
     * This includes information about the mod, such as its name, version, and dependencies.
     */
    val modInfo: ModWrapper

    /**
     * Provides access to the logger used by scafall.
     * This logger can be used to log various messages and events within scafall.
     */
    val logger: Logger
        get() = modInfo.logger

    /**
     * Provides access to the JacksonUtil for the Scafall API.
     * This JacksonUtil can be used to serialize and deserialize JSON data within the Scafall API.
     */
    val jacksonUtil: JacksonUtil

    /**
     * Creates or retrieves a mod wrapper for the specified [modName].
     * If a mod wrapper already exists for the specified mod name, it will be retrieved.
     * If no mod wrapper exists for the specified mod name, a new one will be created.
     *
     * @param modName The name of the mod to create or retrieve a wrapper for.
     * @return The mod wrapper for the specified mod name, or null if no such wrapper exists.
     */
    fun createOrGetPluginWrapper(modName: String) : ModWrapper?

    companion object

}