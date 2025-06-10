plugins {
    `kotlin-dsl`
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
    mavenCentral()
    maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.plugins.devtools.docker.run.text())
    implementation(libs.plugins.devtools.docker.minecraft.text())
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.21")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    target {
        compilations.configureEach {
            kotlinOptions {
                jvmTarget = "21"
            }
        }
    }
}

fun Provider<PluginDependency>.text(): String {
    val t = get()
    val id = t.pluginId
    val version = t.version
    return "$id:$id.gradle.plugin:$version"
}
