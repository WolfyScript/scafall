package com.wolfyscript.scafall.loader;

import com.wolfyscript.scafall.loader.module.ImplementationModuleBootstrap;

public class ScafallLoader {

    private static InnerJarClassloader implementationLoader = null;

    public static ScafallBootstrap loadScafallBootstrap(String pathToInnerJar) {
        implementationLoader = InnerJarClassloader.create(ScafallLoader.class.getClassLoader(), pathToInnerJar);
        return loadBootstrap(implementationLoader);
    }

    /**
     * Loads an inner jar file with access to the internal scafall implementation.
     * By default, plugins depending on scafall can only access the api.
     *
     * @param pathToInnerJar The path to the inner jar to load as a module
     */
    public static <T> ImplementationModuleBootstrap<T> loadImplementationModule(Class<T> bridgeType, String pathToInnerJar, String moduleBootstrapPath) {
        if (implementationLoader == null) {
            throw new RuntimeException("Scafall implementation not loaded yet");
        }
        var moduleLoader = InnerJarClassloader.create(implementationLoader, ScafallLoader.class.getClassLoader(), pathToInnerJar);

        final Class<? extends ImplementationModuleBootstrap<T>> moduleBootstrapImpl;
        try {
            moduleBootstrapImpl = (Class<? extends ImplementationModuleBootstrap<T>>) moduleLoader.loadClass(moduleBootstrapPath).asSubclass(ImplementationModuleBootstrap.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        try {
            return moduleBootstrapImpl.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static ScafallBootstrap loadBootstrap(ClassLoader loader) {
        final Class<? extends ScafallBootstrap> moduleBootstrapImpl;
        try {
            moduleBootstrapImpl = loader.loadClass("com.wolfyscript.scafall.InternalBootstrap").asSubclass(ScafallBootstrap.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not load module bootstrap", e);
        }

        try {
            return moduleBootstrapImpl.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
