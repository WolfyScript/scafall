package com.wolfyscript.scafall.platform

import com.wolfyscript.scafall.identifier.Key

interface PlatformManager {

    val platformType: PlatformType

    fun <T> registerImplementationModule(key: Key, moduleType: Class<T>, innerJarHost: ClassLoader, pathToInnerJar: String, pathToModule: String)

    fun <T> getImplementationModule(key: Key, moduleType: Class<T>): T?

}