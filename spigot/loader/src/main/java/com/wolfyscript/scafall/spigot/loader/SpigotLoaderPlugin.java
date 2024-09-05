package com.wolfyscript.scafall.spigot.loader;

import com.wolfyscript.scafall.loader.InnerJarClassloader;
import com.wolfyscript.scafall.loader.PluginBootstrap;
import com.wolfyscript.scafall.loader.ScafallLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotLoaderPlugin extends JavaPlugin {

    private final PluginBootstrap plugin;

    public SpigotLoaderPlugin() {
        ScafallLoader.initAPIClassLoader(InnerJarClassloader.create(getClassLoader(), "scafall-api.innerjar"));

        var bootstrap = ScafallLoader.loadScafallBootstrap("scafall-spigot.innerjar");
        plugin = bootstrap.initScaffoldingPlatform("com.wolfyscript.scafall.spigot.ScaffoldingSpigotBootstrap", this.getClass(), this);
    }

    @Override
    public void onLoad() {
        plugin.onLoad();
    }

    @Override
    public void onEnable() {
        plugin.onEnable();
    }

    @Override
    public void onDisable() {
        plugin.onUnload();
    }
}
