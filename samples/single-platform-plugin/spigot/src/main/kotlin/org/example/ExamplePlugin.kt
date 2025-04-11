package org.example

import com.wolfyscript.scafall.ScafallProvider
import org.bukkit.plugin.java.JavaPlugin

class ExamplePlugin : JavaPlugin() {

    override fun onLoad() {
        val scaffolding = ScafallProvider.get()

    }

    override fun onEnable() {
    }

    override fun onDisable() {

    }

}