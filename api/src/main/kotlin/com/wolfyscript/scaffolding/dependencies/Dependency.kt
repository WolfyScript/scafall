package com.wolfyscript.scaffolding.dependencies

interface Dependency {

    val group: String

    val name: String

    val version: String

    val fileName: String
        get() = "$name-$version.jar"

    val path: String
        get() = "${group.replace('.', '/')}/$name/$version/$fileName"

}