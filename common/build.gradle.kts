plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
}

dependencies {
    api(project(":api"))
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.common"
            artifactId = "common"
        }
    }
}
