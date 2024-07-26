package com.wolfyscript.scaffolding.common.api.dependencies

import com.wolfyscript.scaffolding.dependencies.Dependency
import com.wolfyscript.scaffolding.dependencies.Repository
import java.io.OutputStream
import java.net.URL
import java.net.URLConnection
import java.nio.file.Path
import kotlin.io.path.outputStream

class RepositoryImpl(
    url: String
) : Repository {

    override val url: String = "$url${
        if (url.endsWith("/")) {
            ""
        } else {
            "/"
        }
    }" // Make sure it always ends with a leading /

    fun connect(dependency: Dependency): URLConnection {
        return URL("$url${dependency.path}").openConnection()
    }

    override fun download(dependency: Dependency, out: OutputStream) {
        val connection = connect(dependency)

        connection.getInputStream().use { input ->
            out.use { out ->
                input.copyTo(out)
            }
        }
    }

    override fun download(dependency: Dependency, file: Path) {
        download(dependency, file.outputStream())
    }


}