package com.wolfyscript.scafall.spigot.api

import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.common.api.ScafallCommon
import com.wolfyscript.scafall.common.api.dependencies.MavenDependencyHandlerImpl
import com.wolfyscript.scafall.common.api.dependencies.MavenRepositoryHandlerImpl
import com.wolfyscript.scafall.common.api.registries.ScafallCommonRegistries
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.scheduling.Scheduler
import com.wolfyscript.scafall.server.ScafallServer
import com.wolfyscript.scafall.spigot.ScafallSpigotBootstrap
import com.wolfyscript.scafall.spigot.api.factories.SpigotFactoriesImpl
import com.wolfyscript.scafall.spigot.api.scheduling.SchedulerImpl
import com.wolfyscript.scafall.spigot.api.platform.SpigotPlatformManager
import com.wolfyscript.scafall.spigot.api.wrappers.utils.SpigotWrapperUtilsImpl
import com.wolfyscript.scafall.spigot.compat.PluginDependencyLoader
import com.wolfyscript.scafall.spigot.server.ScafallSpigotServer
import com.wolfyscript.scafall.wrappers.utils.MinecraftWrapper
import org.bukkit.Bukkit

class ScafallSpigot(internal val bootstrap: ScafallSpigotBootstrap) : ScafallCommon() {

    //
    // Note: This is called before this bridge is registered! ScafallProvider.get() will fail!
    //       Only init things that don't depend on it and use init() instead!
    //

    override val modInfo: ModWrapper = bootstrap.corePlugin

    // Essentials
    override val factories: SpigotFactoriesImpl = SpigotFactoriesImpl(this)
    override val registries: ScafallCommonRegistries = ScafallCommonRegistries(this)

    override val server: ScafallServer = ScafallSpigotServer()
    override val scheduler: Scheduler = SchedulerImpl()
    override val platformManager: SpigotPlatformManager = SpigotPlatformManager(this)
    override val minecraftWrapper: MinecraftWrapper = SpigotWrapperUtilsImpl()
    override val adventure: SpigotAdventureUtil = SpigotAdventureUtil(this)

    override lateinit var mavenDependencyHandler: MavenDependencyHandler
    override lateinit var mavenRepositoryHandler: MavenRepositoryHandler

    //
    // Spigot-only features
    //
    internal val pluginDependencyLoader = PluginDependencyLoader(this)

    override fun init() {
        // initiate essential components
        factories.init()
        registries.initRegistries()
        registries.registerForJackson()

        pluginDependencyLoader.loadDependencies()

        mavenDependencyHandler = MavenDependencyHandlerImpl(this, bootstrap.corePlugin.plugin.dataFolder.toPath().resolve("libs"))
        mavenRepositoryHandler = MavenRepositoryHandlerImpl()
    }

    /**
     * Initiates everything that requires that the plugin instance was created and other plugins are available, but doesn't require the plugin to be enabled.
     */
    override fun load() {
        platformManager.implementationModules.forEach {
            it.value.onLoad()
        }
    }

    /**
     * initiates everything that requires the Spigot Plugin to be enabled.
     * e.g. Adventure, Events, etc.
     */
    override fun enable() {
        adventure.init()
        Bukkit.getPluginManager().registerEvents(pluginDependencyLoader, bootstrap.corePlugin.plugin)

        platformManager.implementationModules.forEach {
            it.value.onEnable()
        }
    }

    override fun unload() {
        platformManager.implementationModules.forEach {
            it.value.onUnload()
        }
        adventure.unload()
    }

    override fun createOrGetPluginWrapper(pluginName: String): ModWrapper? {
        return Bukkit.getPluginManager().getPlugin(pluginName)?.let { SpigotPluginWrapper(it) }
    }

}
