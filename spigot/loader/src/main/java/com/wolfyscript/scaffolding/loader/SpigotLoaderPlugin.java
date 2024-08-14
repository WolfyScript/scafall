package com.wolfyscript.scaffolding.loader;

import org.bukkit.plugin.java.JavaPlugin;

class SpigotLoaderPlugin extends JavaPlugin {

    private final PluginBootstrap plugin;

    public SpigotLoaderPlugin() {
        ScaffoldingLoader.initAPIClassLoader(InnerJarClassloader.create(getClassLoader(), "scaffolding-api.innerjar"));

        var bootstrap = ScaffoldingLoader.loadScaffoldingBootstrap("scaffolding-spigot.innerjar");
        plugin = bootstrap.initScaffoldingPlatform("com.wolfyscript.scaffolding.spigot.ScaffoldingSpigotBootstrap", this);
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
