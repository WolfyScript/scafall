package com.wolfyscript.scafall.sponge.api

import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.common.api.factories.CommonFactories
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.factories.Factories
import com.wolfyscript.scafall.registry.Registries
import com.wolfyscript.scafall.scheduling.Scheduler
import com.wolfyscript.scafall.sponge.api.scheduling.SchedulerImpl
import org.reflections.Reflections

class ScafallSponge : Scafall {

    override val registries: Registries
        get() = TODO("Not yet implemented")
    override val scheduler: Scheduler = SchedulerImpl()
    override val reflections: Reflections
        get() = TODO("Not yet implemented")
    override val mavenDependencyHandler: MavenDependencyHandler
        get() = TODO("Not yet implemented")
    override val mavenRepositoryHandler: MavenRepositoryHandler
        get() = TODO("Not yet implemented")
    override val factories: Factories = CommonFactories()

    override fun createOrGetPluginWrapper(pluginName: String): PluginWrapper {
        TODO("Not yet implemented")
    }

}