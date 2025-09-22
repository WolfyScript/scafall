package com.wolfyscript.scafall.common.api.compat

import com.google.common.collect.Multimaps
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.compat.DependencyManager
import com.wolfyscript.scafall.identifier.Key
import java.util.Collections

class DependencyManagerCommon : DependencyManager {

    private val dependencies = mutableMapOf<Key, Dependency>()
    private val failedDependencies = mutableSetOf<Key>()

    // Init
    private val wildcardInitListeners = mutableListOf<(Dependency) -> Unit>()
    private val initListeners = Multimaps.newListMultimap<Key, (Dependency) -> Unit>(mutableMapOf()) { mutableListOf() }
    private val allInitListeners = mutableListOf<(Map<Key, Dependency>) -> Unit>()

    // Failure
    private val wildcardFailedListeners = mutableListOf<(Dependency) -> Unit>()
    private val failedListeners = Multimaps.newListMultimap<Key, (Dependency) -> Unit>(mutableMapOf()) { mutableListOf() }

    private fun notifyInitListeners(key: Key, dependency: Dependency) {
        for (fn in initListeners[key]) {
            fn(dependency)
        }
        for (fn in wildcardInitListeners) {
            fn(dependency)
        }
        if (allInitListeners.isNotEmpty()) {
            if (dependencies.all { it.value.isInitialized }) {
                val deps = Collections.unmodifiableMap(dependencies)
                for (fn in allInitListeners) {
                    fn(deps)
                }
            }
        }
    }

    private fun notifyFailedListeners(key: Key, dependency: Dependency) {
        for (fn in failedListeners[key]) {
            fn(dependency)
        }
        for (fn in wildcardFailedListeners) {
            fn(dependency)
        }
    }

    override fun loadDependency(
        id: Key,
        dependency: Dependency,
    ) {
        if (!dependencies.containsKey(id) && !failedDependencies.contains(id)) {
            dependencies[id] = dependency
        }
    }

    override fun failedToInitDependency(id: Key) {
        val dependency = dependencies.remove(id)
        failedDependencies.add(id)
        if (dependency != null) {
            notifyFailedListeners(id, dependency)
        }
    }

    override fun initiateDependency(id: Key): Boolean {
        val dependency = dependencies[id] ?: return false
        try {
            dependency.onInit()
        } catch (e: Exception) {
            ScafallProvider.get().logger.error("Failed to initialize dependency $id", e)
            return false
        }
        if (dependency.isInitialized) {
            ScafallProvider.get().logger.info("Initializing dependency $id")
            notifyInitListeners(id, dependency)
            return true
        }
        ScafallProvider.get().logger.warn("Failed to initialize dependency $id")
        return false
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

    override fun onAllDependenciesInitialized(action: (Map<Key, Dependency>) -> Unit) {
        if (dependencies.all { it.value.isInitialized }) {
            action(Collections.unmodifiableMap(dependencies))
            return
        }
        allInitListeners.add(action)
    }

    override fun onDependencyFailed(
        dependency: Key?,
        fn: (Dependency) -> Unit,
    ) {
        if (dependency == null) {
            wildcardFailedListeners.add(fn)
        } else {
            failedListeners[dependency].add(fn)
        }
    }

    override fun onDependencyInitialized(dependency: Key?, fn: (Dependency) -> Unit) {
        if (dependency == null) {
            wildcardInitListeners.add(fn)
        } else {
            initListeners[dependency].add(fn)
        }
    }

}