package com.wolfyscript.scafall.maven

interface MavenDependency {

    val group: String

    val name: String

    val version: String

    val fileName: String
        get() = "$name-$version.jar"

    val path: String
        get() = "${group.replace('.', '/')}/$name/$version/$fileName"

}