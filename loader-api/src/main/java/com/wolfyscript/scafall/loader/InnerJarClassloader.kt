package com.wolfyscript.scafall.loader

import com.wolfyscript.scafall.loader.ScafallLoader.extractJar
import java.net.URL
import java.net.URLClassLoader
import java.util.*


open class InnerJarClassloader internal constructor(
    name: String?,
    parent: ClassLoader?,
    innerJarHost: ClassLoader,
    innerJarPath: String,
) :
    URLClassLoader(name, arrayOf(extractJar(innerJarHost, innerJarPath)), parent) {

        init {
            registerAsParallelCapable()
        }

    fun addJarToClasspath(url: URL?) {
        addURL(url)
    }

    companion object {
        /**
         * Creates a InnerJarLoader where the parent ClassLoader is different from the inner jar host ClassLoader.
         *
         * @param parent The parent ClassLoader
         * @param innerJarHost The host ClassLoader for the inner jar
         * @param innerJarPath The path to the inner jar resource
         * @return A new InnerJarLoader for the specified inner jar
         */
        fun create(parent: ClassLoader?, innerJarHost: ClassLoader, innerJarPath: String): InnerJarClassloader {
            return InnerJarClassloader(null, parent, innerJarHost, innerJarPath)
        }

        fun create(name: String?, parent: ClassLoader?, innerJarHost: ClassLoader, innerJarPath: String): InnerJarClassloader {
            return InnerJarClassloader(name, parent, innerJarHost, innerJarPath)
        }

        /**
         * Creates a InnerJarLoader where the parent ClassLoader is also the host of the inner jar file.
         *
         * @param parent The parent ClassLoader and host ClassLoader of the inner jar
         * @param innerJarPath The path to the inner jar resource
         * @return A new InnerJarLoader for the specified inner jar
         */
        fun create(parent: ClassLoader, innerJarPath: String): InnerJarClassloader {
            return InnerJarClassloader(null, parent, parent, innerJarPath)
        }
    }
}

class ChildFirstInnerJarClassloader(
    name: String? = null,
    parent: ClassLoader?,
    innerJarHost: ClassLoader,
    innerJarPath: String,
    val exclusions: List<Regex> = listOf(),
) :
    InnerJarClassloader(name, parent, innerJarHost, innerJarPath) {

    val system: ClassLoader? = getSystemClassLoader()

    val systemPackages: List<Regex> = listOf(
        "java\\..*".toRegex(),
        "javax\\..*".toRegex(),
        "jdk\\..*".toRegex(),
        "com\\.sun\\..*".toRegex(),
        "kotlin\\..*".toRegex()
    )

    override fun loadClass(name: String?, resolve: Boolean): Class<*>? {
        synchronized(getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            var c = findLoadedClass(name)
            if (c == null) {
                if (system != null) {
                    // Check systempath first to not override those. e.g. jvm classes, endorsed, cmd classpath
                    try {
                        c = system.loadClass(name)
                    } catch (ignore: ClassNotFoundException) {}
                }
                if (c == null) {
                    // Use parent-last approach and check own classpath first
                    c = try {
                        findClass(name)
                    } catch (ignore: ClassNotFoundException) {
                        super.loadClass(name, resolve)
                    }
                }
            }
            if (resolve) {
                resolveClass(c)
            }
            return c
        }
    }

    override fun getResource(name: String?): URL? {
        Objects.requireNonNull<String?>(name)
        var url: URL? = findResource(name)
        if (url == null) {
            url = super.getResource(name)
        }
        if (url == null) {
            url = system?.getResource(name)
        }
        return url
    }

    override fun getResources(name: String?): Enumeration<URL>? {
        /**
         * Similar to super, but local resources are enumerated before parent
         * resources
         */
        var systemUrls: Enumeration<URL>? = null
        if (system != null) {
            systemUrls = system.getResources(name)
        }
        val localUrls = findResources(name)
        var parentUrls: Enumeration<URL>? = null
        if (getParent() != null) {
            parentUrls = getParent().getResources(name)
        }
        val urls: MutableList<URL> = ArrayList<URL>()
        if (localUrls != null) {
            while (localUrls.hasMoreElements()) {
                val local = localUrls.nextElement()
                urls.add(local)
            }
        }
        if (systemUrls != null) {
            while (systemUrls.hasMoreElements()) {
                urls.add(systemUrls.nextElement())
            }
        }
        if (parentUrls != null) {
            while (parentUrls.hasMoreElements()) {
                urls.add(parentUrls.nextElement())
            }
        }
        return object : Enumeration<URL> {
            var iter: MutableIterator<URL> = urls.iterator()

            override fun hasMoreElements(): Boolean {
                return iter.hasNext()
            }

            override fun nextElement(): URL {
                return iter.next()
            }
        }
    }

    override fun findClass(name: String?): Class<*>? {
        val c = super.findClass(name)
        return c
    }

}
