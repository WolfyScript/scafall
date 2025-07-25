plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    alias(libs.plugins.fabric.loom)
}

loom {
    serverOnlyMinecraftJar()

    mods {
        create("scafall") {
            sourceSet(sourceSets.main.get())
        }
    }
}

dependencies {
    api(project(":api"))
    api(project(":common"))
    implementation(project(":loader-api"))

    minecraft("com.mojang:minecraft:${libs.versions.minecraft.get()}")
    mappings(
        loom.layered {
            officialMojangMappings()
//            parchment("org.parchmentmc.data:parchment-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
        }
    )
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modCompileOnly(libs.adventure.platform.shared)
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.fabric"
            artifactId = "scafall-fabric"
        }
    }
}

tasks {

}
