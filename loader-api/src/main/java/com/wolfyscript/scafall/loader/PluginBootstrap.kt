package com.wolfyscript.scafall.loader

/**
 * Implemented by the Scaffolding Platform Bootstraps
 */
interface PluginBootstrap {

    fun onLoad()

    fun onEnable()

    fun onUnload()
}