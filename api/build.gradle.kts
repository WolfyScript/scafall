plugins {
    `java-library`
    `maven-publish`
    id("scaffolding.common")
}
dependencies {
    implementation(kotlin("reflect"))
}