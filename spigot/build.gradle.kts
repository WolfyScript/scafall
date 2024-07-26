plugins {
    `java-library`
    `maven-publish`
    id("scaffolding.common")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":common"))
    compileOnly(libs.papermc.paper)
}
