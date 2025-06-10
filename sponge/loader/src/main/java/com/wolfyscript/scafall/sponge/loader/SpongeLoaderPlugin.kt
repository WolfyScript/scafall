package com.wolfyscript.scafall.sponge.loader;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.wolfyscript.scafall.loader.PluginBootstrap;
import com.wolfyscript.scafall.loader.ScafallBootstrap;
import com.wolfyscript.scafall.loader.ScafallLoader;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("scafall")
public class SpongeLoaderPlugin {

    private Injector injector;
    private PluginBootstrap plugin;

    @Inject
    public SpongeLoaderPlugin(Injector injector, PluginContainer container) {
        this.injector = injector;
        ScafallBootstrap bootstrap = ScafallLoader.loadScafallBootstrap("scafall-sponge.innerjar");
        plugin = bootstrap.initScaffoldingPlatform("com.wolfyscript.scafall.sponge.ScafallSpongeBootstrap", PluginContainer.class, container);
    }

    @Listener
    void onConstructPlugin(ConstructPluginEvent event) {
        plugin.onLoad();
        plugin.onEnable();
    }

    @Listener
    void onServerStarting(StartingEngineEvent<Server> event) {

    }

    @Listener
    void onServerStopping(StoppingEngineEvent<Server> event) {
        this.plugin.onUnload();
    }

    @Listener
    void onRegisterCommands(RegisterCommandEvent<Command.Parameterized> event) {

    }

}
