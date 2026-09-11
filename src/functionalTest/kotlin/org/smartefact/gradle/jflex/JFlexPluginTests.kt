// SPDX-License-Identifier: Apache-2.0

package org.smartefact.gradle.jflex

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JFlexPluginTests {
    lateinit var project: Project

    @BeforeEach
    fun beforeEach() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply(JFlexPlugin.PLUGIN_ID)
    }

    @Test
    fun testJFlexExtension() {
        assertNotNull(project.extensions.findByType(JFlexExtension::class.java))
    }

    @Test
    fun testJFlexConfigurations() {
        assertNotNull(project.configurations.findByName(JFlexPlugin.JFLEX_CONFIGURATION))
        assertNotNull(project.configurations.findByName(JFlexPlugin.JFLEX_CLASSPATH_CONFIGURATION))
    }

    @Test
    fun testDefaultJFlexTask() {
        assertNotNull(project.tasks.findByName(JFlexPlugin.DEFAULT_JFLEX_TASK_NAME))
    }

    @Test
    fun testCustomJFlexTask() {
        val taskName = "testJflex"
        val task = project.tasks.register(taskName, JFlexTask::class.java).get()
        assertTrue(task.outputDirectory.get().asFile.path.contains(taskName))
    }
}
