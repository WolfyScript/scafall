plugins {
    `kotlin-dsl`
}

val wolfyReleasesUrl = providers.gradleProperty("wolfyReleasesUrl").get()
val wolfySnapshotsUrl = providers.gradleProperty("wolfySnapshotsUrl").get()

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven {
        url = uri(wolfyReleasesUrl)
        mavenContent { releasesOnly() }
    }
    maven {
        url = uri(wolfySnapshotsUrl)
        mavenContent { snapshotsOnly() }
    }
}

dependencies {
    implementation(files(libs::class.java.protectionDomain.codeSource.location))
    implementation(files(sharedLibs::class.java.protectionDomain.codeSource.location))
    implementation(sharedLibs.plugins.modrinth.minotaur.text())
    implementation(sharedLibs.plugins.git.changelog.text()) {
        // Drop the old ASM this plugin drags onto the buildSrc (parent) classpath:
        // it shadows the newer ASM that Fabric Loom / tiny-remapper need to
        // read Minecraft 26.3's Java 25 (class file v69) classes.
        exclude(group = "org.openjdk.nashorn", module = "nashorn-core")  // ASM 7.3.1 family
        exclude(group = "net.minidev", module = "accessors-smart")       // ASM 9.7.1
    }
    implementation(sharedLibs.plugins.devtools.docker.run.text())
    implementation(sharedLibs.plugins.devtools.docker.minecraft.text())
    implementation(sharedLibs.plugins.paperweight.userdev.text())
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${sharedLibs.versions.kotlin.get()}")
}

kotlin {
    jvmToolchain(Integer.parseInt(sharedLibs.versions.jdk.get()))
}

fun Provider<PluginDependency>.text(): String {
    val t = get()
    val id = t.pluginId
    val version = t.version
    return "$id:$id.gradle.plugin:$version"
}
