package com.wolfyscript.scafall.loader;

import java.net.URL;
import java.net.URLClassLoader;

public class InnerJarClassloader extends URLClassLoader {

    private InnerJarClassloader(ClassLoader parent, ClassLoader innerJarHost, String innerJarPath) {
        super(new URL[]{ ScafallLoader.INSTANCE.extractJar(innerJarHost, innerJarPath) }, parent);
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

    public void addJarToClasspath(URL url) {
        addURL(url);
    }

}
