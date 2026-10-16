// SPDX-License-Identifier: Apache-2.0

rootProject.name = "jflex-gradle-plugin"

pluginManagement {
    repositories {
        maven("https://jitpack.io") {
            name = "JitPack"
            content {
                includeGroupAndSubgroups("org.smartefact")
            }
        }
        gradlePluginPortal()
        resolutionStrategy {
            eachPlugin {
                if (requested.id.id.startsWith("org.smartefact")) {
                    useModule("org.smartefact:smartefact-gradle-conventions:0.2.0")
                }
            }
        }
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
