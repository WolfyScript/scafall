rootProject.name = "buildSrc" // fix gradle warning with type-safe project accessors

val wolfyReleasesUrl = providers.gradleProperty("wolfyReleasesUrl").get()
val wolfySnapshotsUrl = providers.gradleProperty("wolfySnapshotsUrl").get()

dependencyResolutionManagement {
    repositories {
//        mavenLocal()
        maven {
            url = uri(wolfyReleasesUrl)
            mavenContent { releasesOnly() }
        }
        maven {
            url = uri(wolfySnapshotsUrl)
            mavenContent { snapshotsOnly() }
        }
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
        create("sharedLibs") {
            from("com.wolfyscript.scafall:scafall-versions:2.1.0")
        }
    }
}

pluginManagement {
    val wolfyReleasesUrl = providers.gradleProperty("wolfyReleasesUrl").get()
    val wolfySnapshotsUrl = providers.gradleProperty("wolfySnapshotsUrl").get()

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri(wolfyReleasesUrl)
            mavenContent { releasesOnly() }
        }
        maven {
            url = uri(wolfySnapshotsUrl)
            mavenContent { snapshotsOnly() }
        }
    }
}
