plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
    id("com.jfrog.artifactory")
}

artifactory {
    publish {
        contextUrl = "https://artifacts.wolfyscript.com/artifactory"
        repository {
            repoKey = "gradle-dev-local"
            username = project.findProperty("wolfyRepoPublishUsername").toString()
            password = project.findProperty("wolfyRepoPublishToken").toString()
        }
        defaults {
            publications("lib")
            setPublishArtifacts(true)
            setPublishPom(true)
            isPublishBuildInfo = false
        }
    }
}

