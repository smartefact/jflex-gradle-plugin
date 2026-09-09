// SPDX-License-Identifier: Apache-2.0

package org.smartefact.gradle.jflex

import java.nio.charset.Charset
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.internal.file.FileOperations
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkerExecutor

/**
 * JFlex task.
 *
 * @author Laurent Pireyn
 */
@CacheableTask
abstract class JFlexTask @Inject constructor(
    layout: ProjectLayout
) : DefaultTask() {
    /**
     * Input files.
     */
    @get:InputFiles
    @get:SkipWhenEmpty
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val inputFiles: ConfigurableFileCollection

    fun inputFiles(action: ConfigurableFileCollection.() -> Unit) {
        inputFiles.action()
    }

    /**
     * Encoding.
     *
     * JFlex option: `--encoding`
     *
     * Defaults to `UTF-8`
     */
    @get:Input
    abstract val encoding: Property<String>

    fun encoding(charset: Charset) {
        encoding.set(charset.name())
    }

    /**
     * Output directory.
     *
     * JFlex option: `-d`
     *
     * Defaults to `$buildDir/generated/jflex`
     */
    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    fun outputDirectory(outputDirectory: Any) {
        this.outputDirectory.set(fileOperations.file(outputDirectory))
    }

    /**
     * External skeleton file to use.
     *
     * JFlex option: `--skel`
     */
    @get:InputFile
    @get:Optional
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val skeletonFile: RegularFileProperty

    fun skeletonFile(skeletonFile: Any) {
        this.skeletonFile.set(fileOperations.file(skeletonFile))
    }

    /**
     * Whether to skip the DFA minimization step.
     *
     * JFlex option: `--nomin`
     *
     * Defaults to `false`
     */
    @get:Input
    abstract val skipDfaMin: Property<Boolean>

    /**
     * Whether to try even harder to comply with the JLex interpretation of specs.
     *
     * JFlex option: `--jlex`
     *
     * Defaults to `false`
     */
    @get:Input
    abstract val jlex: Property<Boolean>

    /**
     * Whether to display the transition tables of NFA, initial DFA and minimized DFA.
     *
     * JFlex option: `--dump`
     *
     * Defaults to `false`
     */
    @get:Input
    abstract val dump: Property<Boolean>

    /**
     * Whether the dot (`.`) meta character matches `[^\n]` instead of `[^\n\r\u000B\u000C\u0085\u2028\u2029]`.
     *
     * JFlex option: `--legacydot`
     *
     * Defaults to `false`
     */
    @get:Input
    abstract val legacyDot: Property<Boolean>

    /**
     * Whether to display the error messages only.
     *
     * JFlex option: `-q` or `--quiet`
     *
     * Defaults to `false`
     */
    @get:Input
    abstract val quiet: Property<Boolean>

    /**
     * Whether to display the corresponding warnings.
     *
     * JFlex option: `--warn-unused`
     *
     * Defaults to `false`
     */
    @get:Input
    abstract val warnUnused: Property<Boolean>

    /**
     * Whether to display the corresponding warnings.
     *
     * JFlex option: `--warn-never-match`
     *
     * Defaults to `false`
     */
    @get:Input
    abstract val warnNeverMatch: Property<Boolean>

    /**
     * Whether to display the corresponding warnings.
     *
     * JFlex option: `--warn-empty-match`
     *
     * Defaults to `false`
     */
    @get:Input
    abstract val warnEmptyMatch: Property<Boolean>

    /**
     * Whether to display the corresponding warnings.
     *
     * JFlex option: `--warn-cupsym-after-cup`
     *
     * Defaults to `false`
     */
    @get:Input
    abstract val warnCupsymAfterCup: Property<Boolean>

    /**
     * Whether to display the corresponding warnings.
     *
     * JFlex option: `--warn-unicode-too-long`
     *
     * Defaults to `false`
     */
    @get:Input
    abstract val warnUnicodeTooLong: Property<Boolean>

    /**
     * Whether to display all warnings.
     *
     * JFlex option: `--warn-all`
     *
     * Defaults to `false`
     */
    @get:Input
    abstract val warnAll: Property<Boolean>

    /**
     * JFlex classpath.
     *
     * Defaults to the `jflex` configuration.
     */
    @get:InputFiles
    @get:Classpath
    abstract val jflexClasspath: ConfigurableFileCollection

    private val rootDirectory: Directory = layout.projectDirectory

    @get:Inject
    internal abstract val fileOperations: FileOperations

    @get:Inject
    internal abstract val workerExecutor: WorkerExecutor

    @TaskAction
    fun run() {
        // Prepare parameters
        val inputFiles = inputFiles.files
        assert(inputFiles.isNotEmpty(), { "No input files, task should have been skipped" })
        val encoding = encoding.get()
        val outputDirectory = outputDirectory.get().asFile
        val skeletonFile = skeletonFile.orNull?.asFile
        val skipDfaMin = skipDfaMin.get()
        val jlex = jlex.get()
        val dump = dump.get()
        val legacyDot = legacyDot.get()
        val quiet = quiet.get()
        val warnUnused = warnUnused.get()
        val warnNeverMatch = warnNeverMatch.get()
        val warnEmptyMatch = warnEmptyMatch.get()
        val warnCupsymAfterCup = warnCupsymAfterCup.get()
        val warnUnicodeTooLong = warnUnicodeTooLong.get()
        val warnAll = warnAll.get()
        // Create work queue
        val workQueue = workerExecutor.classLoaderIsolation { spec ->
            spec.classpath.from(jflexClasspath)
        }
        // Submit JFlex work action to work queue for each input file
        for (inputFile in inputFiles) {
            workQueue.submit(JFlexWorkAction::class.java) { parameters ->
                parameters.rootDirectory = rootDirectory.asFile
                parameters.inputFile = inputFile
                parameters.encoding = encoding
                parameters.outputDirectory = outputDirectory
                parameters.skeletonFile = skeletonFile
                parameters.skipDfaMin = skipDfaMin
                parameters.jlex = jlex
                parameters.dump = dump
                parameters.legacyDot = legacyDot
                parameters.quiet = quiet
                parameters.warnUnused = warnUnused
                parameters.warnNeverMatch = warnNeverMatch
                parameters.warnEmptyMatch = warnEmptyMatch
                parameters.warnCupsymAfterCup = warnCupsymAfterCup
                parameters.warnUnicodeTooLong = warnUnicodeTooLong
                parameters.warnAll = warnAll
            }
        }
    }
}
