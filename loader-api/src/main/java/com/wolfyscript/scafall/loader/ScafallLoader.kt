package com.wolfyscript.scafall.loader

import com.wolfyscript.scafall.loader.module.Module
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

object ScafallLoader {

    /**
     * Can be used to load any arbitrary inner-jar module hosted by [innerJarHost] using the specified [loader].
     * It is very likely that [innerJarHost] and [loader] are the same, for example when loading your plugin implementation,
     * from an inner jar.
     */
    fun <T> loadModule(
        moduleType: Class<Module<T>>,
        loader: ClassLoader,
        innerJarHost: ClassLoader,
        pathToInnerJar: String,
        pathToModule: String,
    ): Module<T> {
        return loadObject(moduleType, loader, innerJarHost, pathToInnerJar, pathToModule)
    }

    fun <T> loadModule(
        moduleType: Class<Module<T>>,
        innerJarLoader: InnerJarClassloader,
        pathToModule: String,
    ): Module<T> {
        return loadObject(moduleType, innerJarLoader, pathToModule)
    }

    fun <T> loadObject(
        moduleType: Class<T>,
        loader: ClassLoader,
        innerJarHost: ClassLoader,
        pathToInnerJar: String,
        pathToModule: String,
    ): T {
        return loadObject(
            moduleType,
            InnerJarClassloader.create(loader, innerJarHost, pathToInnerJar),
            pathToModule
        )
    }

    fun <T> loadObject(
        moduleType: Class<T>,
        innerJarLoader: ClassLoader,
        pathToModule: String,
    ): T {
        val moduleClass = try {
            innerJarLoader.loadClass(pathToModule).asSubclass(moduleType)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException("Could not load module", e)
        }

        val module = try {
            moduleClass.getConstructor(InnerJarClassloader::class.java).newInstance(innerJarLoader)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException("Could not load module", e)
        }
        return module
    }

    /**
     * Extracts the specified inner jar to a temporary file. The temporary file is deleted on exit of the program.
     *
     * @param hostLoader The ClassLoader that hosts the inner jar file
     * @param innerJarPath The path to the inner jar resource
     * @return The URL of the extracted temporary jar file
     */
    fun extractJar(hostLoader: ClassLoader, innerJarPath: String): URL {
        val innerJar = hostLoader.getResource(innerJarPath)
        if (innerJar == null) {
            throw java.lang.RuntimeException("Could not locate inner jar: $innerJarPath")
        }

        val path: Path
        try {
            path = Files.createTempFile(innerJarPath, ".jar.tmp")
        } catch (e: IOException) {
            throw java.lang.RuntimeException("Could not create temporary file: $innerJarPath", e)
        }
        path.toFile().deleteOnExit()

        try {
            innerJar.openStream().use { innerJarStream ->
                Files.copy(innerJarStream, path, StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (e: IOException) {
            throw java.lang.RuntimeException("Could not copy temporary file: $path", e)
        }

        try {
            return path.toUri().toURL()
        } catch (e: MalformedURLException) {
            throw java.lang.RuntimeException("Could not convert path to URL: $path", e)
        }
    }
}
