package org.example

import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.loader.ScaffoldingModule

class ExampleModule(val loader: ExamplePlugin, val scaffolding: Scaffolding) : ScaffoldingModule {

    val pluginWrapper = scaffolding.createOrGetPluginWrapper("ExamplePlugin")

    fun loadDependencies() {

    }
}