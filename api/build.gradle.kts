plugins {
    `java-library`
    `maven-publish`
    id("scaffolding.common")
    alias(libs.plugins.goooler.shadow)
}

dependencies {
    compileOnly(project(":loader-api"))
    implementation(kotlin("reflect"))
}

tasks {
    shadowJar {
        archiveFileName = "scaffolding-api.innerjar"

        dependencies {
            include(dependency("com.wolfyscript.scaffolding:.*"))
        }
    }
}
