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
//    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")

    exclusiveContent {
        forRepository {
            maven("https://repo.auxilor.io/repository/maven-public/")
        }
        filter {
            includeGroup("com.willfp")
        }
    }
    exclusiveContent {
        forRepository {
            maven("https://repo.citizensnpcs.co")
        }
        filter {
            includeGroup("com.denizenscript")
            includeGroup("net.citizensnpcs")
            includeGroup("net.citizensnpcs")
        }
    }
    exclusiveContent {
        forRepository {
            maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
        }
        filter {
            includeGroup("me.clip")
        }
    }
    exclusiveContent {
        forRepository {
            maven("https://mvn.lumine.io/repository/maven-public/")
        }
        filter {
            includeGroup("io.lumine")
        }
    }
    exclusiveContent {
        forRepository {
            maven ("https://repo.ssomar.com/releases")
        }
        filter {
            includeGroupAndSubgroups("com.ssomar")
        }
    }
}

kotlin {
    jvmToolchain(Integer.parseInt(sharedLibs.versions.jdk.get()))
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
