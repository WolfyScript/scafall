package com.wolfyscript.scaffolding.sponge.loader

import com.google.inject.Inject
import com.google.inject.Injector
import com.wolfyscript.scaffolding.loader.PluginBootstrap
import com.wolfyscript.scaffolding.loader.ScaffoldingLoader
import com.wolfyscript.scaffolding.loader.InnerJarClassloader
import org.spongepowered.api.Server
import org.spongepowered.api.command.Command
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent
import org.spongepowered.api.event.lifecycle.StartingEngineEvent
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent
import org.spongepowered.plugin.builtin.jvm.Plugin

@Plugin("scaffolding")
class SpongeLoaderPlugin @Inject constructor(private val injector: Injector) {

    private val plugin: PluginBootstrap

    init {
        ScaffoldingLoader.initAPIClassLoader(InnerJarClassloader.create(javaClass.classLoader, "scaffolding-api.innerjar"))
        val bootstrap = ScaffoldingLoader.loadScaffoldingBootstrap("scaffolding-sponge.innerjar")
        plugin = bootstrap.initScaffoldingPlatform("com.wolfyscript.scaffolding.sponge.ScaffoldingSpongeBootstrap", this)
    }

    @Listener
    fun onConstructPlugin(event: ConstructPluginEvent) {
        this.plugin.onLoad()
        this.plugin.onEnable()
    }

    @Listener
    fun onServerStarting(event: StartingEngineEvent<Server>) {

    }

    @Listener
    fun onServerStopping(event: StoppingEngineEvent<Server>) {
        this.plugin.onUnload()
    }

    @Listener
    fun onRegisterCommands(event: RegisterCommandEvent<Command.Parameterized>) {

    }


}