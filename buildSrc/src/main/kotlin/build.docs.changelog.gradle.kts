import com.github.jknack.handlebars.Helper
import com.github.jknack.handlebars.Options
import se.bjurr.gitchangelog.api.model.Commit
import se.bjurr.gitchangelog.internal.semantic.ConventionalCommitParser
import se.bjurr.gitchangelog.plugin.gradle.HelperParam
import utils.getTagAt

plugins {
    id("se.bjurr.gitchangelog.git-changelog-gradle-plugin")
}

tasks {
    gitChangelog {
        file.set(File(".changelog/${project.name}.md"))
        settingsFile.set("${rootProject.projectDir.absolutePath}/.changelog/settings-${project.name}.json")
        templateBaseDir.set("${rootProject.projectDir.absolutePath}/.changelog")
        templateSuffix.set(".partial")
        prependToFile.set(false)
        fromRevision.set(providers.provider {
            getTagAt(1, fromRepo.get())
        })
        toRevision.set(providers.provider {
            getTagAt(0, fromRepo.get())
        })
        templateContent.set(File(".changelog/changelog-common.hbs").readText(Charsets.UTF_8))
    }
}
