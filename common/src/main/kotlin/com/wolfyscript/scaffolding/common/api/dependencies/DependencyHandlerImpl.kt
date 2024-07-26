package com.wolfyscript.scaffolding.common.api.dependencies

import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.dependencies.Dependency
import com.wolfyscript.scaffolding.dependencies.DependencyHandler
import java.nio.file.Path
import kotlin.io.path.exists

class DependencyHandlerImpl(
    private val scaffolding: Scaffolding,
    private val cacheDir: Path
) : DependencyHandler {

    private val loadedDependencies = mutableMapOf<Dependency, Path>()

    override fun createDependency(group: String, name: String, version: String): Dependency {
        return DependencyImpl(group, name, version)
    }

    override fun loadDependency(dependency: Dependency) {
        if (!loadedDependencies.containsKey(dependency)) {
            return
        }

        val source = downloadDependency(dependency)
        loadedDependencies[dependency] = source

    }

    private fun downloadDependency(dependency: Dependency) : Path {
        val cachePath = cacheDir.resolve(dependency.fileName)

        if (cachePath.exists()) {
            return cachePath;
        }

        for (repository in scaffolding.repositoryHandler.repositories) {
            try {
                repository.download(dependency, cachePath)
                return cachePath
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return cachePath
    }
}