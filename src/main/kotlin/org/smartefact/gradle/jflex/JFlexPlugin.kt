// SPDX-License-Identifier: Apache-2.0

package org.smartefact.gradle.jflex

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.language.base.plugins.LifecycleBasePlugin

/**
 * JFlex [Plugin].
 *
 * @author Laurent Pireyn
 */
open class JFlexPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("java")
        // Create `jflex` project extension
        val jflexExtension = project.extensions.create(
            JFlexExtension.NAME,
            JFlexExtension::class.java,
            project
        )
        // Create JFlex-related configurations
        val jflexConfiguration = project.configurations.dependencyScope(JFLEX_CONFIGURATION) { configuration ->
            configuration.description = "JFlex dependencies."
            configuration.defaultDependencies { dependencies ->
                dependencies.add(project.dependencies.create("de.jflex:jflex:${jflexExtension.jflexVersion.get()}"))
            }
        }
        val jflexClasspathConfiguration = project.configurations.resolvable(JFLEX_CLASSPATH_CONFIGURATION) { configuration ->
            configuration.description = "JFlex classpath."
            configuration.extendsFrom(jflexConfiguration)
        }
        // Lazily configure `JFlexTask`s
        project.tasks.withType(JFlexTask::class.java).configureEach { jflexTask ->
            jflexTask.encoding.convention(jflexExtension.encoding)
            jflexTask.outputDirectory.convention(jflexExtension.outputDirectory.orElse(
                project.layout.buildDirectory.dir("generated/sources/${jflexTask.name}/java")
            ))
            jflexTask.skeletonFile.convention(jflexExtension.skeletonFile)
            jflexTask.skipDfaMin.convention(jflexExtension.skipDfaMin)
            jflexTask.jlex.convention(jflexExtension.jlex)
            jflexTask.dump.convention(jflexExtension.dump)
            jflexTask.legacyDot.convention(jflexExtension.legacyDot)
            jflexTask.quiet.convention(jflexExtension.quiet)
            jflexTask.warnUnused.convention(jflexExtension.warnUnused)
            jflexTask.warnNeverMatch.convention(jflexExtension.warnNeverMatch)
            jflexTask.warnEmptyMatch.convention(jflexExtension.warnEmptyMatch)
            jflexTask.warnCupsymAfterCup.convention(jflexExtension.warnCupsymAfterCup)
            jflexTask.warnUnicodeTooLong.convention(jflexExtension.warnUnicodeTooLong)
            jflexTask.warnAll.convention(jflexExtension.warnAll)
            jflexTask.jflexClasspath.setFrom(jflexClasspathConfiguration)
        }
        // Register default `JFlexTask`
        // NOTE: This task is based on the JFlex Maven plugin
        val jflexTask = project.tasks.register(DEFAULT_JFLEX_TASK_NAME, JFlexTask::class.java) { jflexTask ->
            jflexTask.description = "Generates the lexers with JFlex."
            jflexTask.group = LifecycleBasePlugin.BUILD_GROUP
            val inputDirectory = project.layout.projectDirectory.dir("src/main/jflex")
            jflexTask.inputFiles.setFrom(project.fileTree(inputDirectory)
                .include(listOf("jflex", "jlex", "lex", "flex").map { "**/*.$it" })
            )
        }
        val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)
        val mainSourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        mainSourceSet.java.srcDir(jflexTask)
    }

    companion object {
        const val PLUGIN_ID = "org.smartefact.jflex"
        const val JFLEX_CONFIGURATION = "jflex"
        const val JFLEX_CLASSPATH_CONFIGURATION = "jflexClasspath"
        const val DEFAULT_JFLEX_VERSION = "1.9.1"
        const val DEFAULT_JFLEX_TASK_NAME = "jflex"
    }
}
