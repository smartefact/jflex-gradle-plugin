// SPDX-License-Identifier: Apache-2.0

rootProject.name = "jflex-gradle-plugin-example"

pluginManagement {
    repositories {
        includeBuild("..")
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
