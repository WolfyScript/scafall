package com.wolfyscript.scaffolding.loader;

/**
 * Implemented by the Scaffolding Platform Bootstraps
 */
public interface PluginBootstrap {

    void onLoad();

    void onEnable();

    void onUnload();

}