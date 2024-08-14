plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    maven(url = "https://libraries.minecraft.net/")
    maven(url = "https://jitpack.io")
    maven(url = "https://repo.maven.apache.org/maven2/")
}

kotlin {
    jvmToolchain(17)
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Javadoc> {
        options.encoding = "UTF-8"
    }
}

val Project.libs
    get() = the<org.gradle.accessors.dm.LibrariesForLibs>()

dependencies {
    api(libs.jackson.dataformat.hocon)
    implementation(libs.kotlin.stdlib)

    compileOnly(libs.guice)
    compileOnly(libs.reflections)
    compileOnly(libs.fastutil)
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.netty)
    compileOnly(libs.mojang.authlib)
    compileOnly(libs.guava)

    compileOnly(libs.bundles.jackson)
    compileOnlyApi(libs.bundles.adventure)

    testImplementation(libs.junit.jupiter)
}
