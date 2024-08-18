package com.wolfyscript.scaffolding.spigot.api

import com.wolfyscript.scaffolding.AdventureUtil
import com.wolfyscript.scaffolding.PluginWrapper
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.ScaffoldingProvider
import com.wolfyscript.scaffolding.common.api.AbstractScaffoldingImpl
import com.wolfyscript.scaffolding.common.api.dependencies.MavenDependencyHandlerImpl
import com.wolfyscript.scaffolding.common.api.dependencies.MavenRepositoryHandlerImpl
import com.wolfyscript.scaffolding.common.api.registries.CommonRegistries
import com.wolfyscript.scaffolding.maven.MavenDependencyHandler
import com.wolfyscript.scaffolding.maven.MavenRepositoryHandler
import com.wolfyscript.scaffolding.factories.Factories
import com.wolfyscript.scaffolding.platform.PlatformType
import com.wolfyscript.scaffolding.registry.Registries
import com.wolfyscript.scaffolding.scheduling.Scheduler
import com.wolfyscript.scaffolding.spigot.ScaffoldingSpigotBootstrap
import com.wolfyscript.scaffolding.spigot.api.factories.SpigotFactoriesImpl
import com.wolfyscript.scaffolding.spigot.api.scheduling.SchedulerImpl
import com.wolfyscript.scaffolding.spigot.platform.compatibility.CompatibilityManager
import com.wolfyscript.scaffolding.spigot.platform.compatibility.CompatibilityManagerBukkit
import com.wolfyscript.scaffolding.spigot.platform.persistent.PersistentStorage

internal class ScaffoldingSpigot(bootstrap: ScaffoldingSpigotBootstrap) : AbstractScaffoldingImpl() {

    override val registries: Registries = CommonRegistries(ScaffoldingProvider.get())
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
val Scaffolding.persistentStorage : PersistentStorage
    get() = (this as ScaffoldingSpigot).persistentStorageInternal
val Scaffolding.compatibilityManager : CompatibilityManager
    get() = (this as ScaffoldingSpigot).compatibilityManagerInternal