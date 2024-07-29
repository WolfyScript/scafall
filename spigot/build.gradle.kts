plugins {
    `java-library`
    `maven-publish`
    id("scaffolding.common")
    alias(libs.plugins.goooler.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":common"))
    compileOnly(libs.papermc.paper)
}

tasks {
    shadowJar {
        archiveFileName = "scaffolding-spigot.innerjar"

        dependencies {
            include(dependency("com.wolfyscript.scaffolding:.*"))
        }
    }
}

artifacts {
    archives(tasks.shadowJar)
}