plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.publishing")
}

dependencies {
    implementation(kotlin("reflect"))
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.loader"
            artifactId = "loader-api"
            artifact(tasks.kotlinSourcesJar) {
                classifier = "sources"
            }
        }
    }
}
