// SPDX-License-Identifier: Apache-2.0

package org.smartefact.gradle.jflex

import org.gradle.api.Plugin
import org.gradle.api.Project

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
        project.configurations.resolvable(JFLEX_CLASSPATH_CONFIGURATION) { configuration ->
            configuration.description = "JFlex classpath."
            configuration.extendsFrom(jflexConfiguration)
        }
    }

    companion object {
        const val PLUGIN_ID = "org.smartefact.jflex"
        const val JFLEX_CONFIGURATION = "jflex"
        const val JFLEX_CLASSPATH_CONFIGURATION = "jflexClasspath"
        const val DEFAULT_JFLEX_VERSION = "1.9.1"
    }
}
