plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
    id("io.papermc.paperweight.userdev")
}

val Project.libs
    get() = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")

    maven("https://repo.auxilor.io/repository/maven-public/")
    maven(url = "https://maven.enginehub.org/repo/")
    maven(url = "https://repo.citizensnpcs.co")
    maven(url = "https://repo.codemc.io/repository/maven-public/")
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven(url = "https://nexus.phoenixdevt.fr/repository/maven-public/")
    maven(url = "https://mvn.lumine.io/repository/maven-public/")
    maven(url = "https://www.iani.de/nexus/content/repositories/public/")
}

kotlin {
    jvmToolchain(21)
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Javadoc> {
        options.encoding = "UTF-8"
    }
}

dependencies {
    api(libs.jackson.dataformat.hocon)
    implementation(libs.kotlin.stdlib)

    compileOnly(libs.guice)
    compileOnly(libs.fastutil)
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.netty)
    compileOnly(libs.mojang.authlib)

    compileOnlyApi(libs.bundles.adventure)

    testImplementation(libs.junit.jupiter)

    implementation(libs.bundles.jackson)
}
