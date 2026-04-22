package com.wolfyscript.scafall.maven

interface MavenDependencyHandler {

    fun createDependency(group: String, name: String, version: String): MavenDependency

    fun loadDependency(mavenDependency: MavenDependency)

}