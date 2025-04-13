package com.wolfyscript.scafall.spigot.api.platform

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.loader.InnerJarClassloader
import com.wolfyscript.scafall.loader.module.Module
import com.wolfyscript.scafall.platform.PlatformManager
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.spigot.api.ScafallSpigot
import io.papermc.paper.ServerBuildInfo

class SpigotPlatformManager internal constructor(internal val scafallSpigot: ScafallSpigot) : PlatformManager {

    override val platformType: PlatformType = detectPlatform()
    val implementationModules: MutableMap<Key, Module<*>> = mutableMapOf()

    override fun <T> registerImplementationModule(
        key: Key,
        moduleType: Class<T>,
        innerJarHost: ClassLoader,
        pathToInnerJar: String,
        pathToModule: String,
    ) {
        val moduleClassLoader =
            InnerJarClassloader.create(scafallSpigot.bootstrap.classLoader, innerJarHost, pathToInnerJar)
        val moduleClass = moduleClassLoader.loadClass(pathToModule).asSubclass<Module<*>>(Module::class.java)
        val module = moduleClass.getConstructor().newInstance()
        implementationModules.put(key, module)
        return
    }

    override fun <T> getImplementationModule(
        key: Key,
        moduleType: Class<T>,
    ): T? {
        val moduleEntry = implementationModules[key]
        if (moduleEntry == null) {
            return null
        }
        if (!moduleType.isInstance(moduleEntry.bridge)) {
            throw IllegalArgumentException("Failed to get bridge of module ${key}: Expected type ${moduleType}, but got ${moduleEntry.bridge::class}!")
        }
        return moduleEntry.bridge as T
    }

    private fun detectPlatform(): PlatformType {
        val isPaper: Boolean = try {
            Class.forName("io.papermc.paper.ServerBuildInfo")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
        if (isPaper) {
            // We can use the API (which is still experimental though) to check which platform it is
            if (ServerBuildInfo.buildInfo().isBrandCompatible(net.kyori.adventure.key.Key.key("papermc", "folia"))) {
                return PlatformType.FOLIA
            }
            if (ServerBuildInfo.buildInfo().isBrandCompatible(net.kyori.adventure.key.Key.key("purpurmc", "purpur"))) {
                return PlatformType.PURPUR
            }
            return PlatformType.PAPER
        }
        return PlatformType.SPIGOT
    }
}