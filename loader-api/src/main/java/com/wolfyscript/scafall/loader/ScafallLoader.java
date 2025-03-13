package com.wolfyscript.scafall.loader;

public class ScafallLoader {

    public static ScafallBootstrap loadScafallBootstrap(String pathToInnerJar) {
        var classLoader = InnerJarClassloader.create(ScafallLoader.class.getClassLoader(), pathToInnerJar);
        return loadBootstrap(classLoader);
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
