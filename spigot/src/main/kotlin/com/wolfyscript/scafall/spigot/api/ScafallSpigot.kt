package com.wolfyscript.scafall.spigot.api

import com.wolfyscript.scafall.AdventureUtil
import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.common.api.AbstractScafallImpl
import com.wolfyscript.scafall.common.api.dependencies.MavenDependencyHandlerImpl
import com.wolfyscript.scafall.common.api.dependencies.MavenRepositoryHandlerImpl
import com.wolfyscript.scafall.common.api.registries.CommonRegistries
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.registry.Registries
import com.wolfyscript.scafall.scheduling.Scheduler
import com.wolfyscript.scafall.spigot.ScaffoldingSpigotBootstrap
import com.wolfyscript.scafall.spigot.api.factories.SpigotFactoriesImpl
import com.wolfyscript.scafall.spigot.api.scheduling.SchedulerImpl
import com.wolfyscript.scafall.spigot.platform.compatibility.CompatibilityManager
import com.wolfyscript.scafall.spigot.platform.compatibility.CompatibilityManagerBukkit
import com.wolfyscript.scafall.spigot.platform.persistent.PersistentStorage

internal class ScafallSpigot(bootstrap: ScaffoldingSpigotBootstrap) : AbstractScafallImpl() {

    override val registries: Registries = CommonRegistries(ScafallProvider.get())
    override val scheduler: Scheduler = SchedulerImpl(this)
    override val platformType: PlatformType = PlatformType.SPIGOT // TODO: Proper detection
    override val mavenDependencyHandler: MavenDependencyHandler = MavenDependencyHandlerImpl(this, bootstrap.corePlugin.plugin.dataFolder.toPath().resolve("libs"))
    override val mavenRepositoryHandler: MavenRepositoryHandler = MavenRepositoryHandlerImpl()
    override val factories: Factories = SpigotFactoriesImpl()
    override val corePlugin: PluginWrapper = bootstrap.corePlugin
    override val adventure: AdventureUtil
        get() = TODO("Not yet implemented")

    // Spigot only features
    internal val persistentStorageInternal : PersistentStorage = PersistentStorage(this)
    internal val compatibilityManagerInternal : CompatibilityManager = CompatibilityManagerBukkit(this)

    override fun createOrGetPluginWrapper(pluginName: String): PluginWrapper {
        TODO("Not yet implemented")
    }

}

// Provide access to spigot only features without having to cast Scaffolding
val Scafall.persistentStorage : PersistentStorage
    get() = (this as ScafallSpigot).persistentStorageInternal
val Scafall.compatibilityManager : CompatibilityManager
    get() = (this as ScafallSpigot).compatibilityManagerInternal