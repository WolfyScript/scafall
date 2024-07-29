package com.wolfyscript.scaffolding.sponge.loader

import com.google.inject.Inject
import com.google.inject.Injector
import com.wolfyscript.scaffolding.common.api.classloader.InnerJarClassLoader
import com.wolfyscript.scaffolding.common.bootstrap.PluginBootstrap
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
        val classLoader = InnerJarClassLoader(javaClass.classLoader, "scaffolding-sponge.innerjar")
        plugin = classLoader.loadPlugin("com.wolfyscript.scaffolding.sponge.ScaffoldingSpongeBootstrap", this)
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