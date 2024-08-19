plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
}

dependencies {
    implementation(kotlin("reflect"))
}
