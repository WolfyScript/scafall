package com.wolfyscript.scafall.platform

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key

interface PlatformManager {

    val platformType: PlatformType

    fun <T> registerImplementationModule(key: Key, moduleType: Class<T>, innerJarHost: ClassLoader, pathToInnerJar: String, pathToModule: String)

    fun <T> getImplementationModule(key: Key, moduleType: Class<T>): T?

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
