plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.neoforged.net/releases")
        content { includeGroup("org.parchmentmc.data") }
    }
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots1"
        mavenContent { snapshotsOnly() }
    }
    maven(url = "https://central.sonatype.com/repository/maven-snapshots/") {
        name = "central-snapshots"
        mavenContent { snapshotsOnly() }
    }
    maven(url = "https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    maven(url = "https://libraries.minecraft.net/")
    maven(url = "https://jitpack.io")
    maven(url = "https://repo.maven.apache.org/maven2/")
    mavenLocal()
}

kotlin {
    jvmToolchain(25)
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Javadoc> {
        options.encoding = "UTF-8"
    }

// Make sure all tasks which produce archives (jar, sources jar, javadoc jar, etc) produce more consistent output
    withType(AbstractArchiveTask::class).configureEach {
        isReproducibleFileOrder = true
        isPreserveFileTimestamps = false
    }
}

val Project.libs
    get() = the<org.gradle.accessors.dm.LibrariesForLibs>()
val Project.sharedLibs
    get() = the<org.gradle.accessors.dm.LibrariesForSharedLibs>()

dependencies {
    api(sharedLibs.jackson.dataformat.hocon)
    implementation(sharedLibs.kotlinx.coroutines)
    compileOnly(sharedLibs.fastutil)
    compileOnly(sharedLibs.jetbrains.annotations)
    compileOnly(sharedLibs.netty.all)
    compileOnly(sharedLibs.mojang.authlib)
    compileOnly(sharedLibs.slf4j.api)

    implementation(sharedLibs.bundles.jackson)

    compileOnlyApi(sharedLibs.bundles.adventure)

    testImplementation(sharedLibs.junit.jupiter)
}
