plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    alias(sharedLibs.plugins.fabric.loom)
}

dependencies {
    api(project(":core"))
    implementation(project(":loader-api"))
    minecraft(sharedLibs.minecraft)

    compileOnly(sharedLibs.adventure.platform.shared)
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

tasks {

}
