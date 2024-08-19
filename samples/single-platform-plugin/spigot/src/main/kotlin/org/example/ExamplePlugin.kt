package org.example

import com.wolfyscript.scafall.spigot.initOnSpigot
import org.bukkit.plugin.java.JavaPlugin

class ExamplePlugin : JavaPlugin() {

    override fun onLoad() {
        val scaffolding = Scaffolding.initOnSpigot()

    }

    override fun onEnable() {
    }

    override fun onDisable() {

    }

}