// SPDX-License-Identifier: Apache-2.0

package org.smartefact.gradle.jflex

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class JFlexPluginTests {
    @Test
    fun testJFlexPlugin() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(JFlexPlugin.PLUGIN_ID)
        assertTrue(project.pluginManager.hasPlugin(JFlexPlugin.PLUGIN_ID))
        assertNotNull(project.extensions.findByType(JFlexExtension::class.java))
        assertNotNull(project.configurations.findByName(JFlexPlugin.JFLEX_CONFIGURATION))
        assertNotNull(project.configurations.findByName(JFlexPlugin.JFLEX_CLASSPATH_CONFIGURATION))
    }
}
