package com.wolfyscript.scafall.loader

import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

object ScafallLoader {
    private var implementationLoader: InnerJarClassloader? = null
    private var scafallBootstrap: ScafallBootstrap? = null

    @JvmStatic
    fun loadScafallBootstrap(pathToInnerJar: String): ScafallBootstrap {
        implementationLoader = InnerJarClassloader.create(ScafallLoader::class.java.classLoader, pathToInnerJar)
        scafallBootstrap = loadBootstrap(implementationLoader!!)
        return scafallBootstrap!!
    }

    fun loadBootstrap(loader: InnerJarClassloader): ScafallBootstrap {
        val moduleBootstrapImpl: Class<out ScafallBootstrap>
        try {
            moduleBootstrapImpl = loader.loadClass("com.wolfyscript.scafall.InternalBootstrap")
                .asSubclass<ScafallBootstrap>(ScafallBootstrap::class.java)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException("Could not load module bootstrap", e)
        }

        try {
            return moduleBootstrapImpl.getConstructor(InnerJarClassloader::class.java).newInstance(loader)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException(e)
        }
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
