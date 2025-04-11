plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
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
        }
    }
}
