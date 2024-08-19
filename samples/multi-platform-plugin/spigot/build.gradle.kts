plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.papermc.paper)
    api(project(":loader-api"))
    api(project(":api"))
}
