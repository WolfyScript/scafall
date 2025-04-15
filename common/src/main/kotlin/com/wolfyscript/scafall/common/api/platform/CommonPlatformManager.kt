package com.wolfyscript.scafall.common.api.platform

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.loader.InnerJarClassloader
import com.wolfyscript.scafall.loader.module.Module
import com.wolfyscript.scafall.platform.PlatformManager

abstract class CommonPlatformManager(val scafallClassLoader: ClassLoader) : PlatformManager {

    val implementationModules: MutableMap<Key, Module<*>> = mutableMapOf()

    override fun <T> registerImplementationModule(
        key: Key,
        moduleType: Class<T>,
        innerJarHost: ClassLoader,
        pathToInnerJar: String,
        pathToModule: String,
    ) {
        val moduleClassLoader =
            InnerJarClassloader.create(scafallClassLoader, innerJarHost, pathToInnerJar)
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


}