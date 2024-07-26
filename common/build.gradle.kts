plugins {
    `java-library`
    `maven-publish`
    id("scaffolding.common")
}

dependencies {
    api(project(":api"))
}
