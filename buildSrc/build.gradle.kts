plugins {
    `kotlin-dsl`
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
}

dependencies {
    implementation(files(libs::class.java.protectionDomain.codeSource.location))
    implementation(files(sharedLibs::class.java.protectionDomain.codeSource.location))

    implementation(sharedLibs.plugins.devtools.docker.run.text())
    implementation(sharedLibs.plugins.devtools.docker.minecraft.text())
    implementation(sharedLibs.plugins.paperweight.userdev.text())
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${sharedLibs.versions.kotlin.get()}")
}

kotlin {
    jvmToolchain(25)
}

fun Provider<PluginDependency>.text(): String {
    val t = get()
    val id = t.pluginId
    val version = t.version
    return "$id:$id.gradle.plugin:$version"
}
