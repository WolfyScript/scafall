package com.wolfyscript.scafall.sponge.api

import com.wolfyscript.scafall.core.ModIdentifier
import com.wolfyscript.scafall.ScafallCommon
import com.wolfyscript.scafall.registry.ScafallCommonRegistries
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.platform.PlatformManager
import com.wolfyscript.scafall.registry.ScafallRegistries
import com.wolfyscript.scafall.scheduler.Scheduler
import com.wolfyscript.scafall.scheduler.SimpleScheduler
import com.wolfyscript.scafall.sponge.ScafallSpongeBootstrap
import com.wolfyscript.scafall.sponge.api.factories.SpongeFactories
import com.wolfyscript.scafall.sponge.api.platform.PlatformManagerImpl
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import kotlin.jvm.optionals.getOrNull

class ScafallSponge(val bootstrap: ScafallSpongeBootstrap, override val logger: Logger) : ScafallCommon() {

    override lateinit var mavenDependencyHandler: MavenDependencyHandler
    override lateinit var mavenRepositoryHandler: MavenRepositoryHandler
    override lateinit var registries: ScafallRegistries
    override lateinit var scheduler: Scheduler
    override val platformManager: PlatformManager = PlatformManagerImpl(this)
    override lateinit var factories: Factories
    override val identifier: ModIdentifier = bootstrap.corePlugin

    fun init() {
        TODO("Not yet implemented")
    }

    fun load() {
        factories = SpongeFactories(this)
        scheduler = SimpleScheduler(logger)
        registries = ScafallCommonRegistries()

        // maven
    }

    override fun getModIdentifier(pluginName: String): ModIdentifier? {
        return Sponge.pluginManager().plugin(pluginName).getOrNull()?.let { SpongePluginIdentifier(it) }
    }

}