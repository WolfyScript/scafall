dependencyResolutionManagement {
    repositories {
//        mavenLocal()
        maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
        create("sharedLibs") {
            from("com.wolfyscript.scafall:scafall-versions:1.0-SNAPSHOT")
        }
    }
}

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    }
}
