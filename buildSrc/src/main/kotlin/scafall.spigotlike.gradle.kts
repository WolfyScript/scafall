plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
    id("io.papermc.paperweight.userdev")
}

val Project.libs
    get() = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)
val Project.sharedLibs
    get() = the<org.gradle.accessors.dm.LibrariesForSharedLibs>()

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
    jvmToolchain(25)
}



dependencies {
    api(sharedLibs.jackson.dataformat.hocon)
    implementation(sharedLibs.kotlin.stdlib)

//    compileOnly(libs.guice)
    compileOnly(sharedLibs.fastutil)
    compileOnly(sharedLibs.jetbrains.annotations)
    compileOnly(sharedLibs.netty.all)
    compileOnly(sharedLibs.mojang.authlib)

    compileOnlyApi(sharedLibs.bundles.adventure)

    testImplementation(sharedLibs.junit.jupiter)

    implementation(sharedLibs.bundles.jackson)
}
