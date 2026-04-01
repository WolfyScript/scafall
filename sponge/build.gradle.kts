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
    compileOnly(project(":loader-api"))
    api(project(":common"))
    implementation(sharedLibs.slf4j.api)
    compileOnly(sharedLibs.fastutil)
    compileOnly(sharedLibs.jetbrains.annotations)
    compileOnly(libs.spongepowered.impl)
}

//tasks {
//    shadowJar {
//        archiveFileName = "scafall-sponge.innerjar"
//
//        dependencies {
//            include(dependency("com.wolfyscript.scafall:.*"))
//            include(project(":common"))
//        }
//    }
//}
//
//artifacts {
//    archives(tasks.shadowJar)
//}

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
