package com.wolfyscript.scaffolding.loader;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class InnerJarClassloader extends URLClassLoader {

    private InnerJarClassloader(ClassLoader parent, ClassLoader innerJarHost, String innerJarPath) {
        super(new URL[]{ extractJar(innerJarHost, innerJarPath) }, parent);
    }

    /**
     * Creates a InnerJarLoader where the parent ClassLoader is different from the inner jar host ClassLoader.
     *
     * @param parent The parent ClassLoader
     * @param innerJarHost The host ClassLoader for the inner jar
     * @param innerJarPath The path to the inner jar resource
     * @return A new InnerJarLoader for the specified inner jar
     */
    public static InnerJarClassloader create(ClassLoader parent, ClassLoader innerJarHost, String innerJarPath) {
        return new InnerJarClassloader(parent, innerJarHost, innerJarPath);
    }

    /**
     * Creates a InnerJarLoader where the parent ClassLoader is also the host of the inner jar file.
     *
     * @param parent The parent ClassLoader and host ClassLoader of the inner jar
     * @param innerJarPath The path to the inner jar resource
     * @return A new InnerJarLoader for the specified inner jar
     */
    public static InnerJarClassloader create(ClassLoader parent, String innerJarPath) {
        return new InnerJarClassloader(parent, parent, innerJarPath);
    }

    /**
     * Extracts the specified inner jar to a temporary file. The temporary file is deleted on exit of the program.
     *
     * @param hostLoader The ClassLoader that hosts the inner jar file
     * @param innerJarPath The path to the inner jar resource
     * @return The URL of the extracted temporary jar file
     */
    private static URL extractJar(ClassLoader hostLoader, String innerJarPath) {
        var innerJar = hostLoader.getResource(innerJarPath);
        if (innerJar == null) {
            throw new RuntimeException("Could not locate inner jar: " + innerJarPath);
        }

        final Path path;
        try {
            path = Files.createTempFile(innerJarPath, ".jar.tmp");
        } catch (IOException e) {
            throw new RuntimeException("Could not create temporary file: " + innerJarPath, e);
        }
        path.toFile().deleteOnExit();

        try (var innerJarStream = innerJar.openStream()) {
            Files.copy(innerJarStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not copy temporary file: " + path, e);
        }

        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not convert path to URL: " + path, e);
        }
    }

    public void addJarToClasspath(URL url) {
        addURL(url);
    }

    public <T> ScaffoldingModule loadModule(String boostrapClass, T loader) {
        final Class<? extends ScaffoldingModule> moduleClass;
        try {
            moduleClass = loadClass(boostrapClass).asSubclass(ScaffoldingModule.class);
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
