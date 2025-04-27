plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    alias(libs.plugins.fabric.loom)
}

dependencies {
    api(project(":api"))
    implementation(project(":loader-api"))
    minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")
    mappings(loom.officialMojangMappings())
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
