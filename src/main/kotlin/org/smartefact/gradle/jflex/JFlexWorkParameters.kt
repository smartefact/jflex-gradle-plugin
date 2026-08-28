// SPDX-License-Identifier: Apache-2.0

package org.smartefact.gradle.jflex

import java.io.File
import org.gradle.workers.WorkParameters

/**
 * [WorkParameters] for [JFlexWorkAction].
 *
 * @author Laurent Pireyn
 */
internal interface JFlexWorkParameters : WorkParameters {
    var rootDirectory: File
    var inputFile: File
    var encoding: String
    var outputDirectory: File
    var skeletonFile: File?
    var skipDfaMin: Boolean
    var jlex: Boolean
    var dump: Boolean
    var legacyDot: Boolean
    var quiet: Boolean
    var warnUnused: Boolean
    var warnNeverMatch: Boolean
    var warnEmptyMatch: Boolean
    var warnCupsymAfterCup: Boolean
    var warnUnicodeTooLong: Boolean
    var warnAll: Boolean
}
