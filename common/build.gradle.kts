plugins {
    `java-library`
    `maven-publish`
    id("scaffolding.common")
}

dependencies {
    api(project(":api"))
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scaffolding.common"
            artifactId = "common"
        }
    }
}
