package com.wolfyscript.scafall.common.api.dependencies

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.maven.MavenDependency
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import java.nio.file.Path
import kotlin.io.path.exists

class MavenDependencyHandlerImpl(
    private val scafall: Scafall,
    private val cacheDir: Path
) : MavenDependencyHandler {

    private val loadedDependencies = mutableMapOf<MavenDependency, Path>()

    override fun createDependency(group: String, name: String, version: String): MavenDependency {
        return MavenDependencyImpl(group, name, version)
    }

    override fun loadDependency(mavenDependency: MavenDependency) {
        if (!loadedDependencies.containsKey(mavenDependency)) {
            return
        }
        val source = downloadDependency(mavenDependency)
        loadedDependencies[mavenDependency] = source

    }

    private fun downloadDependency(mavenDependency: MavenDependency) : Path {
        val cachePath = cacheDir.resolve(mavenDependency.fileName)

        if (cachePath.exists()) {
            return cachePath;
        }

        for (repository in scafall.mavenRepositoryHandler.repositories) {
            try {
                repository.download(mavenDependency, cachePath)
                return cachePath
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return cachePath
    }
}