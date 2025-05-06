package com.wolfyscript.scafall.spigot.api

import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.common.api.AbstractScafallImpl
import com.wolfyscript.scafall.common.api.dependencies.MavenDependencyHandlerImpl
import com.wolfyscript.scafall.common.api.dependencies.MavenRepositoryHandlerImpl
import com.wolfyscript.scafall.common.api.factories.CommonFactories
import com.wolfyscript.scafall.common.api.registries.CommonRegistries
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.registry.Registries
import com.wolfyscript.scafall.scheduling.Scheduler
import com.wolfyscript.scafall.spigot.ScafallSpigotBootstrap
import com.wolfyscript.scafall.spigot.api.data.PaperItemStackDataComponentConverter
import com.wolfyscript.scafall.spigot.api.data.SpigotItemStackDataComponentConverterProvider
import com.wolfyscript.scafall.spigot.api.factories.SpigotFactoriesImpl
import com.wolfyscript.scafall.spigot.api.scheduling.SchedulerImpl
import com.wolfyscript.scafall.spigot.api.platform.SpigotPlatformManager
import com.wolfyscript.scafall.spigot.platform.compatibility.CompatibilityManager
import com.wolfyscript.scafall.spigot.platform.compatibility.CompatibilityManagerBukkit
import com.wolfyscript.scafall.spigot.platform.persistent.PersistentStorage
import com.wolfyscript.scafall.spigot.api.wrappers.utils.SpigotWrapperUtilsImpl
import com.wolfyscript.scafall.wrappers.utils.MinecraftWrapper
import org.bukkit.Bukkit

internal class ScafallSpigot(internal val bootstrap: ScafallSpigotBootstrap) : AbstractScafallImpl() {

    override lateinit var registries: Registries
    override lateinit var scheduler: Scheduler
    override val platformManager: SpigotPlatformManager = SpigotPlatformManager(this)
    override lateinit var mavenDependencyHandler: MavenDependencyHandler
    override lateinit var mavenRepositoryHandler: MavenRepositoryHandler
    override val factories: CommonFactories = SpigotFactoriesImpl(this)
    override var corePlugin: PluginWrapper = bootstrap.corePlugin
    override lateinit var adventure: SpigotAdventureUtil
    override val minecraftWrapper: MinecraftWrapper = SpigotWrapperUtilsImpl()

    // Spigot only features
    internal lateinit var persistentStorageInternal : PersistentStorage
    internal lateinit var compatibilityManagerInternal : CompatibilityManager

    override fun createOrGetPluginWrapper(pluginName: String): PluginWrapper? {
        return Bukkit.getPluginManager().getPlugin(pluginName)?.let { SpigotPluginWrapper(it) }
    }

    override fun load() {
        factories.init()

        scheduler = SchedulerImpl(this)
        registries = CommonRegistries(this)

        // maven
        mavenDependencyHandler = MavenDependencyHandlerImpl(this, bootstrap.corePlugin.plugin.dataFolder.toPath().resolve("libs"))
        mavenRepositoryHandler = MavenRepositoryHandlerImpl()

        persistentStorageInternal = PersistentStorage(this)
        compatibilityManagerInternal = CompatibilityManagerBukkit(this)

        adventure = SpigotAdventureUtil(this)

        if (platformManager.platformType == PlatformType.SPIGOT) {
            SpigotItemStackDataComponentConverterProvider(this).register()
        } else {
            PaperItemStackDataComponentConverter(this).register()
        }

        platformManager.implementationModules.forEach {
            it.value.onLoad()
        }
    }

    override fun enable() {
        adventure.init()

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

}

// Provide access to spigot only features without having to cast Scaffolding
val Scafall.persistentStorage : PersistentStorage
    get() = (this as ScafallSpigot).persistentStorageInternal
val Scafall.compatibilityManager : CompatibilityManager
    get() = (this as ScafallSpigot).compatibilityManagerInternal