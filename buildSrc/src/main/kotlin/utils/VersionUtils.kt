package utils

import org.gradle.api.Project
import se.bjurr.gitchangelog.api.GitChangelogApi
import kotlin.collections.joinToString
import kotlin.collections.mapIndexed
import kotlin.text.indexOfFirst
import kotlin.text.split
import kotlin.text.substring
import kotlin.text.toInt
import kotlin.to

fun Project.archiveName(type: String, mcVersion: String): String = "${rootProject.name}-${version}-${type}-${mcVersion}"

fun getTagAt(index: Int, repo: String): String {
    val tags = GitChangelogApi.gitChangelogApiBuilder().withFromRepo(repo).changelog.tags
    val tag = tags.getOrNull(index)?.name
    return tag ?: "HEAD"
}
