package com.wolfyscript.scaffolding.dependencies

interface DependencyHandler {

    fun createDependency(group: String, name: String, version: String): Dependency

    fun loadDependency(dependency: Dependency)

}