plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
//    alias(sharedLibs.plugins.shadow)
}

repositories {
    mavenCentral()
    maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
    implementation(projects.core)
    compileOnly(project(":loader-api"))
    implementation(sharedLibs.slf4j.api)
    compileOnly(sharedLibs.fastutil)
    compileOnly(sharedLibs.jetbrains.annotations)
    compileOnly(libs.spongepowered.impl)
}

minecraft {
    version("1.21.5") // or: latestRelease() or latestSnapshot()
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.sponge"
            artifactId = "sponge"
        }
    }
}
