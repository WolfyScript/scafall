package com.wolfyscript.scaffolding.dependencies

import java.io.OutputStream
import java.nio.file.Path

interface Repository {

    val url: String

    fun download(dependency: Dependency, out: OutputStream)

    fun download(dependency: Dependency, file: Path)

}