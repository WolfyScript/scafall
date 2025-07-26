package com.wolfyscript.scafall.common.api

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.common.api.compat.DependencyManagerCommon
import com.wolfyscript.scafall.common.api.jackson.JacksonUtilImpl
import com.wolfyscript.scafall.compat.DependencyManager
import com.wolfyscript.scafall.config.jackson.JacksonUtil
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder

abstract class ScafallCommon() : Scafall {

    override val jacksonUtil: JacksonUtil = JacksonUtilImpl()
    override val dependencyManager: DependencyManager = DependencyManagerCommon()

    override val reflections: Reflections = Reflections(
        ConfigurationBuilder()
            .forPackage("com.wolfyscript", javaClass.classLoader)
            .addClassLoaders(javaClass.classLoader)
            .addScanners(*Scanners.entries.toTypedArray())
    )

}