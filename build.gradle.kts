import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask

plugins {
    id("com.jfrog.artifactory") version "5.+"
    kotlin("jvm") version "2.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks {
    test {
        useJUnitPlatform()
    }
    withType<ArtifactoryTask> {
        skip = true
    }
}

kotlin {
    jvmToolchain(17)
}

artifactory {
    publish {
        contextUrl = "https://artifacts.wolfyscript.com/artifactory"
        repository {
            repoKey = "gradle-dev-local"
            username = project.properties["wolfyRepoPublishUsername"].toString()
            password = project.properties["wolfyRepoPublishToken"].toString()
        }
        defaults {
            publications("lib")
            setPublishArtifacts(true)
            setPublishPom(true)
            isPublishBuildInfo = false
        }
    }
}
