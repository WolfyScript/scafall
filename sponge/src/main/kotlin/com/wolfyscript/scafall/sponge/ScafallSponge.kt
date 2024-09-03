package com.wolfyscript.scafall.sponge

import com.wolfyscript.scafall.AdventureUtil
import com.wolfyscript.scafall.PluginWrapper
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
import com.wolfyscript.scafall.sponge.api.SpongeAdventureUtil
import com.wolfyscript.scafall.sponge.api.factories.SpongeFactories
import com.wolfyscript.scafall.sponge.api.scheduling.SchedulerImpl
import kotlin.io.path.Path

class ScafallSponge(private val bootstrap: ScaffoldingSpongeBootstrap) : AbstractScafallImpl() {

    override lateinit var mavenDependencyHandler: MavenDependencyHandler
    override lateinit var mavenRepositoryHandler: MavenRepositoryHandler
    override lateinit var registries: Registries
    override lateinit var scheduler: Scheduler
    override lateinit var factories: Factories
    override val adventure: AdventureUtil = SpongeAdventureUtil(this)
    override val corePlugin: PluginWrapper = bootstrap.corePlugin
    override val platformType: PlatformType = PlatformType.SPONGE

    override fun load() {
        factories = SpongeFactories()
        scheduler = SchedulerImpl()
        registries = CommonRegistries(this)

        // maven
        mavenDependencyHandler = MavenDependencyHandlerImpl(this, Path("")) // TODO
        mavenRepositoryHandler = MavenRepositoryHandlerImpl()
    }

    override fun enable() {

    }

    override fun unload() {

    }

    override fun createOrGetPluginWrapper(pluginName: String): PluginWrapper {
        TODO("Not yet implemented")
    }

}