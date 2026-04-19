package com.wolfyscript.scafall.sponge.api

import com.wolfyscript.scafall.adventure.AdventureUtil
import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.common.api.ScafallCommon
import com.wolfyscript.scafall.common.api.dependencies.MavenDependencyHandlerImpl
import com.wolfyscript.scafall.common.api.dependencies.MavenRepositoryHandlerImpl
import com.wolfyscript.scafall.common.api.registries.ScafallCommonRegistries
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.platform.PlatformManager
import com.wolfyscript.scafall.registry.ScafallRegistries
import com.wolfyscript.scafall.scheduling.Scheduler
import com.wolfyscript.scafall.scheduling.SimpleScheduler
import com.wolfyscript.scafall.server.ScafallServer
import com.wolfyscript.scafall.sponge.ScafallSpongeBootstrap
import com.wolfyscript.scafall.sponge.api.factories.SpongeFactories
import com.wolfyscript.scafall.sponge.api.platform.PlatformManagerImpl
import com.wolfyscript.scafall.sponge.server.ScafallSpongeServer
import com.wolfyscript.scafall.wrappers.MinecraftWrapper
import org.spongepowered.api.Sponge
import kotlin.io.path.Path
import kotlin.jvm.optionals.getOrNull

class ScafallSponge(val bootstrap: ScafallSpongeBootstrap) : ScafallCommon() {

    override lateinit var mavenDependencyHandler: MavenDependencyHandler
    override lateinit var mavenRepositoryHandler: MavenRepositoryHandler
    override lateinit var registries: ScafallRegistries
    override lateinit var scheduler: Scheduler
    override val platformManager: PlatformManager = PlatformManagerImpl(this)
    override lateinit var factories: Factories
    override val adventure: AdventureUtil = SpongeAdventureUtil(this)
    override val minecraftWrapper: MinecraftWrapper
        get() = TODO("Not yet implemented")
    override val modInfo: ModWrapper = bootstrap.corePlugin
    override val server: ScafallServer = ScafallSpongeServer()

    override fun init() {
        TODO("Not yet implemented")
    }

    override fun load() {
        factories = SpongeFactories(this)
        scheduler = SimpleScheduler()
        registries = ScafallCommonRegistries(this)

        // maven
        mavenDependencyHandler = MavenDependencyHandlerImpl(this, Path("")) // TODO
        mavenRepositoryHandler = MavenRepositoryHandlerImpl()
    }

    override fun enable() {
    }

    override fun unload() {

    }

    override fun createOrGetPluginWrapper(pluginName: String): ModWrapper? {
        return Sponge.pluginManager().plugin(pluginName).getOrNull()?.let { SpongePluginWrapper(it) }
    }

}