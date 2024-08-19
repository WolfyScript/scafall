package org.example

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.loader.ScaffoldingModule

class ExampleModule(val loader: ExamplePlugin, val scafall: Scafall) : ScaffoldingModule {

    val pluginWrapper = scafall.createOrGetPluginWrapper("ExamplePlugin")

    fun loadDependencies() {

    }
}