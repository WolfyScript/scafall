plugins {
    kotlin("jvm")
    alias(sharedLibs.plugins.fabric.loom) apply false
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks {
    test {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(Integer.parseInt(sharedLibs.versions.jdk.get()))
}
