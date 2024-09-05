import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

val Project.libs
    get() = the<org.gradle.accessors.dm.LibrariesForLibs>()

plugins {
    id("com.wolfyscript.devtools.docker.run")
    id("com.wolfyscript.devtools.docker.minecraft_servers")
}

val debugPort: String = System.getenv("debugPort") ?: "5006"
minecraftDockerRun {
//    clean.set(false)
    val customEnv = env.get().toMutableMap()
    customEnv["MEMORY"] = "2G"
    customEnv["JVM_OPTS"] = "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${debugPort}"
    customEnv["FORCE_REDOWNLOAD"] = "false"
    env.set(customEnv)
    arguments("--cpus", "2", "-it") // Constrain to only use 2 cpus, and allow for console interactivity with 'docker attach'
}
