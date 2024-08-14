plugins {
    `java-library`
    `maven-publish`
    id("scaffolding.common")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.papermc.paper)
    api(project(":loader-api"))
    api(project(":api"))
}
