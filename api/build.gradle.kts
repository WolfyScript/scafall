plugins {
    `java-library`
    id("scafall.common")
    alias(libs.plugins.shadow)
}

dependencies {
    compileOnly(project(":loader-api"))
    implementation(kotlin("reflect"))
}

tasks {
    shadowJar {
        archiveFileName = "scafall-api.innerjar"

        dependencies {
            include(dependency("com.wolfyscript.scafall:.*"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall"
            artifactId = "api"
        }
    }
}
