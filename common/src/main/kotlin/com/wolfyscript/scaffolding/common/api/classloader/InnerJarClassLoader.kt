package com.wolfyscript.scaffolding.common.api.classloader

import com.wolfyscript.scaffolding.common.bootstrap.PluginBootstrap
import java.io.IOException
import java.lang.reflect.Constructor
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class InnerJarClassLoader(parent: ClassLoader, pathToJar: String) : URLClassLoader(arrayOf(extractJar(parent, pathToJar)), parent) {

    companion object {

        private fun extractJar(hostClassLoader: ClassLoader, innerJarPath: String) : URL {
            val innerJar = hostClassLoader.getResource(innerJarPath)
                ?: throw IOException("Could not locate inner jar: $innerJarPath")

            val path: Path = try {
                Files.createTempFile(innerJarPath, ".jar.tmp")
            } catch (e: IOException) {
                throw IOException("Could not create temporary file: $innerJarPath", e)
            }

            path.toFile().deleteOnExit()

            val innerJarStream = innerJar.openStream()

            try {
                innerJarStream.use {
                    Files.copy(it, path, StandardCopyOption.REPLACE_EXISTING)
                }
            } catch (e: IOException) {
                throw IOException("Could not copy temporary file: $path", e)
            }

            return path.toUri().toURL()
        }

    }

    fun addJarToClasspath(url: URL) {
        addURL(url)
    }

    inline fun <reified T> loadPlugin(bootstrapClass: String, pluginLoader: T) : PluginBootstrap {
        val plugin: Class<out PluginBootstrap> = try {
            loadClass(bootstrapClass).asSubclass(PluginBootstrap::class.java)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Unable to load plugin bootstrap class $bootstrapClass", e)
        }

        val constructor: Constructor<out PluginBootstrap> = try {
            plugin.getConstructor(T::class.java)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Unable to find plugin bootstrap constructor $bootstrapClass", e)
        }

        return try {
            constructor.newInstance(pluginLoader)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Could not create plugin bootstrap instance", e)
        }
    }

}