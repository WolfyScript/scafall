package com.wolfyscript.scafall.common.api.platform

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.loader.InnerJarClassloader
import com.wolfyscript.scafall.loader.module.Module
import com.wolfyscript.scafall.platform.PlatformManager

abstract class CommonPlatformManager(val scafallClassLoader: ClassLoader) : PlatformManager {

    val implementationModules: MutableMap<Key, Module<*,*>> = mutableMapOf()

    override fun <T: Module<*,*>> registerImplementationModule(
        key: Key,
        moduleType: Class<T>,
        innerJarHost: ClassLoader,
        pathToInnerJar: String,
        pathToModule: String,
    ) {
        val moduleClassLoader =
            InnerJarClassloader.create(scafallClassLoader, innerJarHost, pathToInnerJar)
        val moduleClass = moduleClassLoader.loadClass(pathToModule).asSubclass<Module<*,*>>(Module::class.java)
        val module = moduleClass.getConstructor().newInstance()
        implementationModules[key] = module
        module.onInit()
        return
    }

    override fun <T: Module<*,*>> registerModule(
        key: Key,
        supplier: () -> T,
    ): T {
        val module = supplier()
        implementationModules[key] = module
        module.onInit()
        return module
    }

    override fun <T: Module<*,*>> getImplementationModule(
        key: Key,
        moduleType: Class<T>,
    ): T? {
        val moduleEntry = implementationModules[key] ?: return null
        if (!moduleType.isInstance(moduleEntry)) {
            throw IllegalArgumentException("Failed to get bridge of module ${key}: Expected type ${moduleType}, but got ${moduleEntry::class}!")
        }
        return moduleType.cast(moduleEntry)
    }

}


