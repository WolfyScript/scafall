package com.wolfyscript.scaffolding.sponge.api

import com.wolfyscript.scaffolding.PluginWrapper
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.common.api.factories.CommonFactories
import com.wolfyscript.scaffolding.maven.MavenDependencyHandler
import com.wolfyscript.scaffolding.maven.MavenRepositoryHandler
import com.wolfyscript.scaffolding.factories.Factories
import com.wolfyscript.scaffolding.registry.Registries
import com.wolfyscript.scaffolding.scheduling.Scheduler
import com.wolfyscript.scaffolding.sponge.api.scheduling.SchedulerImpl
import org.reflections.Reflections

class ScaffoldingSponge : Scaffolding {

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