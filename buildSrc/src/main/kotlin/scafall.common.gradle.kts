plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.parchmentmc.org/")
        content { includeGroup("org.parchmentmc.data") }
    }
    maven {
        url = uri("https://maven.neoforged.net/releases")
        content { includeGroup("org.parchmentmc.data") }
    }
    maven(url = "https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    maven(url = "https://libraries.minecraft.net/")
    maven(url = "https://jitpack.io")
    maven(url = "https://repo.maven.apache.org/maven2/")
    mavenLocal()
}

kotlin {
    jvmToolchain(21)
}

val javaTarget = 21
java {
    sourceCompatibility = JavaVersion.toVersion(javaTarget)
    targetCompatibility = JavaVersion.toVersion(javaTarget)
    if (JavaVersion.current() < JavaVersion.toVersion(javaTarget)) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaTarget))
    }
}

tasks.withType(JavaCompile::class).configureEach {
    options.apply {
        encoding = "utf-8" // Consistent source file encoding
        release.set(javaTarget)
    }
}

// Make sure all tasks which produce archives (jar, sources jar, javadoc jar, etc) produce more consistent output
tasks.withType(AbstractArchiveTask::class).configureEach {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
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
val Project.sharedLibs
    get() = the<org.gradle.accessors.dm.LibrariesForSharedLibs>()

dependencies {
    api(sharedLibs.jackson.dataformat.hocon)
    implementation(sharedLibs.kotlin.stdlib)

//    compileOnly(libs.guice)
    compileOnly(sharedLibs.fastutil)
    compileOnly(sharedLibs.jetbrains.annotations)
    compileOnly(sharedLibs.netty.all)
    compileOnly(sharedLibs.mojang.authlib)
    compileOnly(sharedLibs.slf4j.api)

    implementation(sharedLibs.bundles.jackson)

    compileOnlyApi(sharedLibs.bundles.adventure)

    testImplementation(sharedLibs.junit.jupiter)
}
