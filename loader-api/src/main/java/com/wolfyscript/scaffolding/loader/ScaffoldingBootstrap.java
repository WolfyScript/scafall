package com.wolfyscript.scaffolding.loader;

import org.jetbrains.annotations.ApiStatus;

public interface ScaffoldingBootstrap {

    ScaffoldingModule createScaffoldingModule(String entrypoint, Object loader);

    /**
     * Used by Scaffolding itself to initialise Platform implementations.
     * For your own plugin use {@link #createScaffoldingModule(String, Object)} instead!
     *
     * @param pathToBootstrap The path to the bootstrap implementation inside the inner jar file
     * @param loader The loader that was used as the entrypoint (Platform specific plugin)
     * @return The Bootstrap for the platform implementation
     */
    @ApiStatus.Internal
    PluginBootstrap initScaffoldingPlatform(String pathToBootstrap, Object loader);

}
