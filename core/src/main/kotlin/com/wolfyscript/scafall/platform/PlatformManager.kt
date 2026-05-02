package com.wolfyscript.scafall.platform

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.loader.module.Module

/**
 * Manages platform-specific module registration and retrieval.
 *
 * This interface provides methods to register implementation modules that are loaded from inner JARs,
 * as well as generic modules supplied via a factory function. It also allows fetching registered modules
 * based on their keys and expected types, abstracting away the underlying platform specifics.
 *
 * The [platformType] property indicates the current execution platform, which can be used to conditionally
 * execute code tailored for specific platforms such as SPIGOT, PAPER, PURPUR, FOLIA, SPONGE, or FABRIC.
 */
interface PlatformManager {

    /**
     * Indicates the current execution platform.
     */
    val platformType: PlatformType

    /**
     * Registers an implementation module that is loaded from an inner JAR.
     *
     * @param key the unique identifier for the module, used to retrieve it later
     * @param moduleType the expected type of the module being registered
     * @param innerJarHost the class loader hosting the inner JAR containing the module
     * @param pathToInnerJar the file system path to the inner JAR within the host's resources
     * @param pathToModule the fully qualified name (including package) of the module class inside the inner JAR
     */
    fun <T: Module<*,*>> registerImplementationModule(key: Key, moduleType: Class<T>, innerJarHost: ClassLoader, pathToInnerJar: String, pathToModule: String)

    /**
     * Registers a module that can be lazily instantiated.
     *
     * This method allows registering a module using a supplier function that provides the module instance.
     * The supplied module is stored under the provided key, enabling retrieval by the same key later.
     *
     * @param key the unique identifier for the registered module
     * @param supplier a lambda that returns an instance of the module type `T`
     * @return the newly registered module instance of type `T`
     */
    fun <T: Module<*,*>> registerModule(key: Key, supplier: () -> T): T

    /**
     * Retrieves an implementation module registered under the specified key and of the expected type.
     *
     * @param key The unique identifier for the module to retrieve.
     * @param moduleType The expected runtime type of the module being retrieved.
     * @return An instance of the requested [Module] if found and matches the type; otherwise, null.
     */
    fun <T: Module<*,*>> getImplementationModule(key: Key, moduleType: Class<T>): T?

}

/**
 * Runs the given [then] function if the current [PlatformManager.platformType] is equal to the specified [platform].
 */
fun <R> ifPlatform(platform: PlatformType, then: () -> R): R? {
    return ifPlatform(platform, then, null)
}

/**
 * Runs the given [then] function if the current [PlatformManager.platformType] is equal to the specified [platform].
 * Otherwise, runs the [orElse] function.
 */
fun <R> ifPlatform(platform: PlatformType, then: () -> R, orElse: (() -> R)? = null): R? {
    if (ScafallProvider.get().platformManager.platformType == platform) {
        return then()
    }
    return orElse?.invoke()
}

/**
 * Runs the given [then] function if the current [PlatformManager.platformType] is [PlatformType.isPaperCompatible].
 */
fun <R> ifPaperCompatible(then: () -> R): R? {
    return ifPaperCompatible(then, null)
}

/**
 * Runs the given [then] function if the current [PlatformManager.platformType] is [PlatformType.isPaperCompatible].
 * Otherwise, runs the [orElse] function.
 */
fun <R> ifPaperCompatible(then: () -> R, orElse: (() -> R)? = null): R? {
    if (ScafallProvider.get().platformManager.platformType.isPaperCompatible()) {
        return then()
    }
    return orElse?.invoke()
}
