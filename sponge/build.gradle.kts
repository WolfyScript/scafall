plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    alias(libs.plugins.shadow)
}

repositories {
    mavenCentral()
    maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
    compileOnly(project(":loader-api"))
    implementation(project(":common"))
    implementation(libs.slf4j.api)
    compileOnly(libs.reflections)
    compileOnly(libs.fastutil)
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.spongepowered.api)
}

tasks {
    shadowJar {
        archiveFileName = "scafall-sponge.innerjar"

        dependencies {
            include(dependency("com.wolfyscript.scafall:.*"))
            include(project(":common"))
        }
    }
}

artifacts {
    archives(tasks.shadowJar)
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
