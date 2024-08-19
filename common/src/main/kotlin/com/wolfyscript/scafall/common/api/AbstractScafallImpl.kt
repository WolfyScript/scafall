package com.wolfyscript.scafall.common.api

import com.wolfyscript.scafall.Scafall
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

abstract class AbstractScafallImpl : Scafall {

    override val reflections: Reflections = Reflections(
        ConfigurationBuilder()
            .forPackage("")
            .addUrls(ClasspathHelper.forClassLoader())
            .forPackage("com.wolfyscript", javaClass.classLoader)
            .addClassLoaders(javaClass.classLoader)
            .addScanners(*Scanners.entries.toTypedArray())
    )

    fun enable() {

    }


}