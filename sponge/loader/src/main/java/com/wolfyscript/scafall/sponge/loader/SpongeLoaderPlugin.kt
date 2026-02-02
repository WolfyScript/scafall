package com.wolfyscript.scafall.sponge.loader

import com.google.inject.Inject
import com.google.inject.Injector
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallBootstrap
import com.wolfyscript.scafall.loader.ScafallLoader.loadObject
import com.wolfyscript.scafall.loader.module.Module
import org.spongepowered.api.Server
import org.spongepowered.api.command.Command
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent
import org.spongepowered.api.event.lifecycle.StartingEngineEvent
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent
import org.spongepowered.plugin.PluginContainer
import org.spongepowered.plugin.builtin.jvm.Plugin

@Plugin("scafall")
class SpongeLoaderPlugin @Inject constructor(private val injector: Injector?, container: PluginContainer) {
    private val module: Module<Scafall>

    init {
        val bootstrap = loadObject(
            ScafallBootstrap::class.java,
            this::class.java.classLoader,
            this::class.java.classLoader,
            "scafall-sponge.innerjar",
            "com.wolfyscript.scafall.InternalBootstrap"
        )
        module = bootstrap.loadModule(
            "com.wolfyscript.scafall.sponge.ScafallSpongeBootstrap",
            PluginContainer::class.java,
            container
        )
    }

    @Listener
    fun onConstructPlugin(event: ConstructPluginEvent?) {
        module.onLoad()
        module.onEnable()
    }

    @Listener
    fun onServerStarting(event: StartingEngineEvent<Server?>?) {
    }

    @Listener
    fun onServerStopping(event: StoppingEngineEvent<Server?>?) {
        this.module.onUnload()
    }

    @Listener
    fun onRegisterCommands(event: RegisterCommandEvent<Command.Parameterized?>?) {
    }
}
