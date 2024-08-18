package com.wolfyscript.scaffolding.maven

import java.io.OutputStream
import java.nio.file.Path

interface MavenRepository {

    val url: String

    fun download(mavenDependency: MavenDependency, out: OutputStream)

    fun download(mavenDependency: MavenDependency, file: Path)

}