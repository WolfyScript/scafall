package com.wolfyscript.scafall.common.api

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.common.api.jackson.JacksonUtilImpl
import com.wolfyscript.scafall.config.jackson.JacksonUtil
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder

abstract class AbstractScafallImpl : Scafall {

    override val jacksonUtil: JacksonUtil = JacksonUtilImpl()

    override val reflections: Reflections = Reflections(
        ConfigurationBuilder()
            .forPackage("com.wolfyscript", javaClass.classLoader)
            .addClassLoaders(javaClass.classLoader)
            .addScanners(*Scanners.entries.toTypedArray())
    )

    abstract fun load()

    abstract fun enable()

    abstract fun unload()
}