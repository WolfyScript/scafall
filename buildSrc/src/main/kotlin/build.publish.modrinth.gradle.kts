import masecla.modrinth4j.model.version.ProjectVersion
import org.gradle.accessors.dm.LibrariesForSharedLibs
import org.gradle.kotlin.dsl.getByType
import java.util.Locale

plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
    id("com.modrinth.minotaur")
}

val Project.sharedLibs
    get() = extensions.getByType(LibrariesForSharedLibs::class)

private fun determineVersionType(): ProjectVersion.VersionType {
    val split = project.version.toString().split("-")
    return when {
        split.size < 2 -> ProjectVersion.VersionType.RELEASE
        split[1].startsWith("alpha") -> ProjectVersion.VersionType.ALPHA
        split[1].startsWith("beta") -> ProjectVersion.VersionType.BETA
        else -> ProjectVersion.VersionType.ALPHA
    }
}

private fun createPlatformProjectChangelog(): String {
    val changelogDir = file("$rootDir/.changelog/")
    val customNotes = File(changelogDir, "custom-notes.md").readText()
    val platformProject = File(changelogDir, "${project.name}.md").readText()

    val wrapperChangelog = File(changelogDir, "wrappers.md").readText()
    val identifierChangelog = File(changelogDir, "identifiers.md").readText()
    val schedulerChangelog = File(changelogDir, "scheduler.md").readText()

    return buildString {
        if (customNotes.isNotBlank()) append(customNotes)
        if (platformProject.isNotBlank()) appendLine().append(platformProject)

        if (wrapperChangelog.isNotBlank()) appendLine().append("## Wrappers").append(wrapperChangelog)
        if (identifierChangelog.isNotBlank()) appendLine().append("## Identifiers").append(identifierChangelog)
        if (schedulerChangelog.isNotBlank()) appendLine().append("## Scheduler").append(schedulerChangelog)
    }
}

modrinth {
    token.set(findProperty("modrinthPublishToken")?.toString() ?: System.getenv("MODRINTH_TOKEN"))
    projectId.set("scafall")
    debugMode.set(true)

    versionNumber.set(project.version.toString())
    versionType.set(determineVersionType().toString().lowercase(Locale.ROOT))
    changelog.set(createPlatformProjectChangelog())
    gameVersions.addAll(sharedLibs.versions.minecraft.get())
}
