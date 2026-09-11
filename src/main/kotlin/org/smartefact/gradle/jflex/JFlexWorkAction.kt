// SPDX-License-Identifier: Apache-2.0

package org.smartefact.gradle.jflex

import jflex.Main
import jflex.exceptions.GeneratorException
import jflex.exceptions.SilentExit
import jflex.option.Options
import org.gradle.api.GradleException
import org.gradle.api.logging.Logging
import org.gradle.workers.WorkAction

/**
 * JFlex [WorkAction].
 *
 * @author Laurent Pireyn
 */
internal abstract class JFlexWorkAction : WorkAction<JFlexWorkParameters> {
    override fun execute() {
        val rootDirectory = parameters.rootDirectory
        val inputFile = parameters.inputFile
        val encoding = parameters.encoding
        val outputDirectory = parameters.outputDirectory
        val skeletonFile = parameters.skeletonFile
        val skipDfaMin = parameters.skipDfaMin
        val jlex = parameters.jlex
        val dump = parameters.dump
        val legacyDot = parameters.legacyDot
        val quiet = parameters.quiet
        val warnUnused = parameters.warnUnused
        val warnNeverMatch = parameters.warnNeverMatch
        val warnEmptyMatch = parameters.warnEmptyMatch
        val warnCupsymAfterCup = parameters.warnCupsymAfterCup
        val warnUnicodeTooLong = parameters.warnUnicodeTooLong
        val warnAll = parameters.warnAll
        // Determine package and compute actual output directory
        val charset = JFlexPlugin.encodingToCharset(encoding)
        val actualOutputDirectory = inputFile.bufferedReader(charset = charset).lineSequence()
            .mapNotNull { line -> PACKAGE_REGEX.matchEntire(line) }
            .firstOrNull()
            ?.groupValues[1]
            ?.let {
                it.split('.')
                    .fold(outputDirectory) { directory, name ->
                        // Skip blank elements in package name and let the Java compiler complain later
                        if (name.isNotBlank()) directory.resolve(name) else directory
                    }
            }
            ?: outputDirectory
        // Compute JFlex args
        val args = mutableListOf<String>()
        fun addOptionArg(option: String, value: Any? = true) {
            if (value != null && value != false) {
                args += option
                if (value != true) {
                    args += value.toString()
                }
            }
        }
        addOptionArg("--encoding", encoding)
        addOptionArg("-d", actualOutputDirectory.path)
        addOptionArg("--skel", skeletonFile?.path)
        addOptionArg("--nomin", skipDfaMin)
        addOptionArg("--jlex", jlex)
        addOptionArg("--dump", dump)
        addOptionArg("--legacydot", legacyDot)
        addOptionArg("--quiet", quiet)
        addOptionArg("--warn-unused", warnUnused)
        addOptionArg("--warn-never-match", warnNeverMatch)
        addOptionArg("--warn-empty-match", warnEmptyMatch)
        addOptionArg("--warn-cupsym-after-cup", warnCupsymAfterCup)
        addOptionArg("--warn-unicode-too-long", warnUnicodeTooLong)
        addOptionArg("--warn-all", warnAll)
        // This is not configurable as it makes no sense in a Gradle project
        addOptionArg("--nobak")
        args += inputFile.path
        // Run JFlex
        LOGGER.info("Running JFlex (args={})", args)
        // IMPORTANT: the root directory must be set in the JFlex options,
        // or a `NullPointerException` is thrown
        Options.setRootDirectory(rootDirectory)
        // IMPORTANT: the `Main.main` method must not be invoked here,
        // as it results in a call to `System.exit`
        try {
            Main.generate(args.toTypedArray())
        } catch (e: GeneratorException) {
            throw GradleException("JFlex threw a ${e.javaClass.simpleName}", e)
        } catch (e: SilentExit) {
            val exitCode = e.exitCode()
            if (exitCode != 0) {
                throw GradleException("JFlex exited with error code $exitCode", e)
            }
        }
    }

    companion object {
        private val PACKAGE_REGEX = Regex("\\s*package\\s+([^;\\s]+)\\s*;\\s*")
        private val LOGGER = Logging.getLogger(JFlexWorkAction::class.java)
    }
}
