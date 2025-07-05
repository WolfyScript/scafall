package com.wolfyscript.scafall.common.api.platform

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.loader.InnerJarClassloader
import com.wolfyscript.scafall.loader.module.Module
import com.wolfyscript.scafall.platform.PlatformManager
import com.wolfyscript.scafall.platform.PlatformType

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
        module.onInit()
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

/**
 * Runs the given function if the current [PlatformManager.platformType] is equal to the specified [platform]
 */
fun ifPlatform(platform: PlatformType, fn: () -> Unit) {
    if (ScafallProvider.get().platformManager.platformType == platform) {
        fn()
    }
}

/**
 * Runs the given [then] function if the current [PlatformManager.platformType] is equal to the specified [platform].
 * Otherwise, runs the [orElse] function.
 */
fun ifPlatformOrElse(platform: PlatformType, then: () -> Unit, orElse: () -> Unit) {
    if (ScafallProvider.get().platformManager.platformType == platform) {
        then()
        return
    }
    orElse()
}
