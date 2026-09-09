// SPDX-License-Identifier: Apache-2.0

package org.smartefact.gradle.jflex

import java.nio.charset.Charset
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Optional

/**
 * JFlex project extension.
 *
 * @author Laurent Pireyn
 */
abstract class JFlexExtension(
    private val project: Project
) {
    /**
     * JFlex version to use.
     *
     * Default: [JFlexPlugin.DEFAULT_JFLEX_VERSION]
     */
    abstract val jflexVersion: Property<String>
    init {
        jflexVersion.convention(JFlexPlugin.DEFAULT_JFLEX_VERSION)
    }

    /**
     * Encoding.
     *
     * JFlex option: `--encoding`
     *
     * Defaults to `UTF-8`
     */
    abstract val encoding: Property<String>
    init {
        encoding.convention(Charsets.UTF_8.name())
    }

    fun encoding(charset: Charset) {
        encoding.set(charset.name())
    }

    /**
     * Output directory.
     *
     * JFlex option: `-d`
     *
     * Defaults to `$buildDir/generated/sources/jflex/java`
     */
    abstract val outputDirectory: DirectoryProperty
    init {
        outputDirectory.convention(project.layout.buildDirectory.dir("generated/sources/jflex/java"))
    }

    fun outputDirectory(outputDirectory: Any) {
        this.outputDirectory.set(project.file(outputDirectory))
    }

    /**
     * External skeleton file to use.
     *
     * JFlex option: `--skel`
     */
    @get:Optional
    abstract val skeletonFile: RegularFileProperty

    fun skeletonFile(skeletonFile: Any) {
        this.skeletonFile.set(project.file(skeletonFile))
    }

    /**
     * Whether to skip the DFA minimization step.
     *
     * JFlex option: `--nomin`
     *
     * Defaults to `false`
     */
    abstract val skipDfaMin: Property<Boolean>
    init {
        skipDfaMin.convention(false)
    }

    /**
     * Whether to try even harder to comply with the JLex interpretation of specs.
     *
     * JFlex option: `--jlex`
     *
     * Defaults to `false`
     */
    abstract val jlex: Property<Boolean>
    init {
        jlex.convention(false)
    }

    /**
     * Whether to display the transition tables of NFA, initial DFA and minimized DFA.
     *
     * JFlex option: `--dump`
     *
     * Defaults to `false`
     */
    abstract val dump: Property<Boolean>
    init {
        dump.convention(false)
    }

    /**
     * Whether the dot (`.`) meta character matches `[^\n]` instead of `[^\n\r\u000B\u000C\u0085\u2028\u2029]`.
     *
     * JFlex option: `--legacydot`
     *
     * Defaults to `false`
     */
    abstract val legacyDot: Property<Boolean>
    init {
        legacyDot.convention(false)
    }

    /**
     * Whether to display the error messages only.
     *
     * JFlex option: `-q` or `--quiet`
     *
     * Defaults to `false`
     */
    abstract val quiet: Property<Boolean>
    init {
        quiet.convention(false)
    }

    /**
     * Whether to display the corresponding warnings.
     *
     * JFlex option: `--warn-unused`
     *
     * Defaults to `false`
     */
    abstract val warnUnused: Property<Boolean>
    init {
        warnUnused.convention(false)
    }

    /**
     * Whether to display the corresponding warnings.
     *
     * JFlex option: `--warn-never-match`
     *
     * Defaults to `false`
     */
    abstract val warnNeverMatch: Property<Boolean>
    init {
        warnNeverMatch.convention(false)
    }

    /**
     * Whether to display the corresponding warnings.
     *
     * JFlex option: `--warn-empty-match`
     *
     * Defaults to `false`
     */
    abstract val warnEmptyMatch: Property<Boolean>
    init {
        warnEmptyMatch.convention(false)
    }

    /**
     * Whether to display the corresponding warnings.
     *
     * JFlex option: `--warn-cupsym-after-cup`
     *
     * Defaults to `false`
     */
    abstract val warnCupsymAfterCup: Property<Boolean>
    init {
        warnCupsymAfterCup.convention(false)
    }

    /**
     * Whether to display the corresponding warnings.
     *
     * JFlex option: `--warn-unicode-too-long`
     *
     * Defaults to `false`
     */
    abstract val warnUnicodeTooLong: Property<Boolean>
    init {
        warnUnicodeTooLong.convention(false)
    }

    /**
     * Whether to display all warnings.
     *
     * JFlex option: `--warn-all`
     *
     * Defaults to `false`
     */
    abstract val warnAll: Property<Boolean>
    init {
        warnAll.convention(false)
    }

    companion object {
        const val NAME = "jflex"
    }
}
