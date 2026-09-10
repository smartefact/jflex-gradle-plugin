// SPDX-License-Identifier: Apache-2.0

import org.gradle.plugin.compatibility.compatibility

plugins {
    id("org.smartefact.kotlin-gradle-plugin")
    id("org.smartefact.junit-jupiter")
    id("org.smartefact.gradle-plugin-publish")
}

version = "0.2.0"
description = "Gradle plugin for JFlex."

dependencies {
    compileOnly("de.jflex:jflex:1.9.1")
}

gradlePlugin {
    website = "https://github.com/smartefact/jflex-gradle-plugin"
    vcsUrl = "https://github.com/smartefact/jflex-gradle-plugin.git"
    plugins {
        register("org.smartefact.jflex") {
            displayName = "JFlex Gradle plugin"
            description = "Gradle plugin for JFlex."
            tags = listOf("jflex")
            implementationClass = "org.smartefact.gradle.jflex.JFlexPlugin"
            compatibility {
                features {
                    configurationCache = true
                }
            }
        }
    }
}
