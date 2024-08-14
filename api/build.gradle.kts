plugins {
    `java-library`
    id("scaffolding.common")
    alias(libs.plugins.goooler.shadow)
}

dependencies {
    compileOnly(project(":loader-api"))
    implementation(kotlin("reflect"))
}

tasks {
    shadowJar {
        archiveFileName = "scaffolding-api.innerjar"

        dependencies {
            include(dependency("com.wolfyscript.scaffolding:.*"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scaffolding"
            artifactId = "api"
        }
    }
}
