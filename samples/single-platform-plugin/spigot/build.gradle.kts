plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.papermc.paper)
    api(libs.adventure.api)
    api(libs.adventure.text.minimessage)
    api(libs.adventure.platform.bukkit)

    api(project(":spigot:platform"))
}

kotlin {
    jvmToolchain(17)
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Javadoc> {
        options.encoding = "UTF-8"
    }

    withType<ProcessResources> {
        expand(project.properties)
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}
