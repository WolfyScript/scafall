package com.wolfyscript.scaffolding.loader;

import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Constructor;

public class ScaffoldingLoader {

    private static ClassLoader sharedApiClassLoader = null;

    /**
     * The task of the API ClassLoader is to load the Scaffolding API and provide it to its child classloaders!
     * Reason being that this encapsulates the API classes and the APIs dependencies.
     * The benefit is that the API requires no relocation, and it can easily be accessed
     * without having to also relocate each plugin that tries to use the API.
     * Downside, plugins that want to use the API need to compile and load a separate module to do so.
     *
     * @return The API Classloader, required for API access
     */
    static ClassLoader getSharedApiClassLoader() {
        if (sharedApiClassLoader == null) {
            throw new IllegalStateException("Scaffolding API not loaded yet!");
        }
        return sharedApiClassLoader;
    }

    @ApiStatus.Internal
    public static void initAPIClassLoader(ClassLoader classloader) {
        if (sharedApiClassLoader != null) {
            throw new IllegalStateException("Scaffolding API already initialized!");
        }
        sharedApiClassLoader = classloader;
    }

    /**
     * Loads the entrypoint from specified inner jar using a new classloader that provides access to the Scaffolding API.
     * The Scaffolding API is independent of other modules.
     *
     * @param parentClassLoader
     * @param pathToInnerJar
     */
    public static ScaffoldingModule loadIndependentPlatformModule(ClassLoader parentClassLoader, String pathToInnerJar, String entrypoint, Object loader) {
        // Create independent api class loader. Load the API from the inner jar into this new class loader
        var encapsulatedAPILoader = InnerJarClassloader.create(parentClassLoader, ScaffoldingLoader.class.getClassLoader(), "scaffolding-api.innerJar");
        var classLoader = InnerJarClassloader.create(encapsulatedAPILoader, pathToInnerJar);
        return loadBootstrap(classLoader).createScaffoldingModule(entrypoint, loader);
    }

    /**
     * Loads the entrypoint from the specified inner jar using the scaffolding classloader that shares the API between all modules, that use this method of loading.<br>
     * <b>Only works when scaffolding is loaded via the external runtime plugin!</b>
     *
     * @param pathToInnerJar
     * @param entrypoint
     * @return
     */
    public static ScaffoldingModule loadPlatformModule(String pathToInnerJar, String entrypoint, Object loader) {
        // Load the API from the shared API class loader, that was created by the Scaffolding Plugin
        return loadScaffoldingBootstrap(pathToInnerJar).createScaffoldingModule(entrypoint, loader);
    }

    public static ScaffoldingBootstrap loadScaffoldingBootstrap(String pathToInnerJar) {
        var classLoader = InnerJarClassloader.create(getSharedApiClassLoader(), pathToInnerJar);
        return loadBootstrap(classLoader);
    }

    public static ScaffoldingBootstrap loadBootstrap(ClassLoader loader) {
        final Class<? extends ScaffoldingBootstrap> moduleBootstrapImpl;
        try {
            moduleBootstrapImpl = loader.loadClass("com.wolfyscript.scaffolding.InternalBootstrap").asSubclass(ScaffoldingBootstrap.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not load module bootstrap", e);
        }

        try {
            return moduleBootstrapImpl.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> ScaffoldingModule loadModule(ClassLoader classLoader, String boostrapClass, T loader) {
        final Class<? extends ScaffoldingModule> moduleClass;
        try {
            moduleClass = classLoader.loadClass(boostrapClass).asSubclass(ScaffoldingModule.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to load module: " + boostrapClass, e);
        }

        final Constructor<? extends ScaffoldingModule> constructor;
        try {
            constructor = moduleClass.getConstructor(loader.getClass());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to find constructor for module: " + boostrapClass, e);
        }

        final ScaffoldingModule module;
        try {
            module = constructor.newInstance(loader);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        return module;
    }

}
