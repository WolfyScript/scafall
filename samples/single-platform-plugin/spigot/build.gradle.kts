plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(sharedLibs.papermc.paper)
    implementation("com.wolfyscript.scafall:api:alpha0.0.1.0-SNAPSHOT")
    api(sharedLibs.adventure.api)
    api(sharedLibs.adventure.minimessage)
    api(sharedLibs.adventure.platform.bukkit)
}

kotlin {
    jvmToolchain(25)
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
