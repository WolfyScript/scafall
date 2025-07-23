package com.wolfyscript.scafall.common.api.compat

import com.google.common.collect.Multimaps
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.compat.DependencyManager
import com.wolfyscript.scafall.identifier.Key

class DependencyManagerCommon : DependencyManager {

    private val dependencies = mutableMapOf<Key, Dependency>()

    private val wildcardInitListeners = mutableListOf<(Dependency) -> Unit>()
    private val initListeners = Multimaps.newListMultimap<Key, (Dependency) -> Unit>(mutableMapOf()) { mutableListOf() }

    override fun loadDependency(
        id: Key,
        dependency: Dependency,
    ) {
        if (!dependencies.containsKey(id)) {
            dependencies[id] = dependency
        }
    }

    override fun initiateDependency(id: Key): Boolean {
        val dependency = dependencies[id]
        if (dependency == null) {
            return false
        }
        for (fn in initListeners[id]) {
            fn(dependency)
        }
        return true
    }

    override fun getDependency(id: Key): Dependency? {
        return dependencies[id]
    }

    override fun isLoaded(id: Key): Boolean {
        return dependencies.containsKey(id)
    }

    override fun <R> runIfLoaded(dependency: Key, action: () -> R): R? {
        if (isLoaded(dependency)) {
            return action()
        }
        return null
    }

    override fun <R> runIfInitialized(dependency: Key, action: () -> R): R? {
        if (dependencies[dependency]?.isInitialized == true) {
            return action()
        }
        return null
    }

    override fun onDependencyInitialized(dependency: Key?, fn: (Dependency) -> Unit) {
        if (dependency == null) {
            wildcardInitListeners.add(fn)
        } else {
            initListeners[dependency].add(fn)
        }
    }

}