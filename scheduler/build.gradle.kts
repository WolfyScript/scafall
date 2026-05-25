plugins {
    `java-library`
    id("scafall.publishing")
    id("scafall.common")
}

dependencies {
    implementation(kotlin("reflect"))
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.scheduler"
            artifactId = "scheduler"
            artifact(tasks.kotlinSourcesJar) {
                classifier = "sources"
            }
        }
    }
}
