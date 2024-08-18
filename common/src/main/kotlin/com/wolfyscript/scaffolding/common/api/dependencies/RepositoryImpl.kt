package com.wolfyscript.scaffolding.common.api.dependencies

import com.wolfyscript.scaffolding.maven.MavenDependency
import com.wolfyscript.scaffolding.maven.MavenRepository
import java.io.OutputStream
import java.net.URL
import java.net.URLConnection
import java.nio.file.Path
import kotlin.io.path.outputStream

class RepositoryImpl(
    url: String
) : MavenRepository {

    override val url: String = "$url${
        if (url.endsWith("/")) {
            ""
        } else {
            "/"
        }
    }" // Make sure it always ends with a leading /

    private fun connect(mavenDependency: MavenDependency): URLConnection {
        return URL("$url${mavenDependency.path}").openConnection()
    }

    override fun download(mavenDependency: MavenDependency, out: OutputStream) {
        val connection = connect(mavenDependency)

        connection.getInputStream().use { input ->
            out.use { out ->
                input.copyTo(out)
            }
        }
    }

    override fun download(mavenDependency: MavenDependency, file: Path) {
        download(mavenDependency, file.outputStream())
    }


}