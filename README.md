# JFlex Gradle plugin

[Gradle](https://gradle.org/) plugin for [JFlex](https://www.jflex.de/).

[![Release](https://img.shields.io/github/v/release/smartefact/jflex-gradle-plugin?logo=github&color=blue
)](https://github.com/smartefact/jflex-gradle-plugin/releases/latest)
[![GitHub Workflow](https://github.com/smartefact/jflex-gradle-plugin/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/smartefact/jflex-gradle-plugin/actions/workflows/ci.yml?query=branch%3Amain)
[![GitHub Issues](https://img.shields.io/github/issues/smartefact/jflex-gradle-plugin?logo=github)](https://github.com/smartefact/jflex-gradle-plugin/issues)
[![Apache 2.0](https://img.shields.io/badge/license-Apache_2.0-green)](https://www.apache.org/licenses/LICENSE-2.0)
[![Common Changelog](https://common-changelog.org/badge.svg)](https://common-changelog.org)

## Usage

Apply the JFlex plugin:

```kotlin
plugins {
    id("org.smartefact.jflex") version "0.1.0"
}
```

Configure the JFlex extension:

```kotlin
jflex {
    warnAll = true
    // Other properties...
}
```

[More about the JFlex extension...](#jflex-extension)

Create a JFlex task and configure it:

```kotlin
val jflex = tasks.register<JFlexTask>("jflex") {
    inputFiles.from("src/jflex/MyRules.flex")
    // Other properties...
}
```

The JFlex plugin determines the destination packages of the generated files by parsing the `package` declaration in the input files;
the actual location of the input files is ignored.

[More about the JFlex task...](#jflex-task)

Add the JFlex task to a Java source set:

```kotlin
sourceSets.main {
    java {
        srcDirs(jflex)
    }
}
```

## JFlex extension

The `jflex` extension allows you to define properties shared by all JFlex tasks.

It has the following properties:

| Property             | Description                                                                                             | Default value               |
|----------------------|---------------------------------------------------------------------------------------------------------|-----------------------------|
| `jflexVersion`       | Version of JFlex                                                                                        | `1.9.1`                     |
| `encoding`           | Encoding of the JFlex and generated Java files                                                          | `UTF-8`                     |
| `outputDirectory`    | Output directory                                                                                        | `$buildDir/generated/jflex` |
| `skeletonFile`       | External skeleton file to use                                                                           |                             |
| `skipDfaMin`         | Whether to skip the DFA minimization step                                                               | `false`                     |
| `jlex`               | Whether to try even harder to comply with the JLex interpretation of specs                              | `false`                     |
| `dump`               | Whether to display the transition tables of NFA, initial DFA and minimized DFA                          | `false`                     |
| `legacyDot`          | Whether the dot (`.`) meta character matches `[^\n]` instead of `[^\n\r\u000B\u000C\u0085\u2028\u2029]` | `false`                     |
| `quiet`              | Whether to display the error messages only                                                              | `false`                     |
| `warnUnused`         | Whether to display the corresponding warnings                                                           | `false`                     |
| `warnNeverMatch`     | Whether to display the corresponding warnings                                                           | `false`                     |
| `warnEmptyMatch`     | Whether to display the corresponding warnings                                                           | `false`                     |
| `warnCupsymAfterCup` | Whether to display the corresponding warnings                                                           | `false`                     |
| `warnUnicodeTooLong` | Whether to display the corresponding warnings                                                           | `false`                     |
| `warnAll`            | Whether to display all warnings                                                                         | `false`                     |

## JFlex task

The `JFlexTask` task runs JFlex on a set of input files.

It inherits all the properties from the `jflex` extension and adds the following property:

| Property     | Description                                           | Default value |
|--------------|-------------------------------------------------------|---------------|
| `inputFiles` | Input files (the task will be skipped if it is empty) |               |

## Unsupported JFlex options

The following JFlex options are not supported by this plugin:

- `--nobak`: Always enabled by this plugin
- `--verbose`: Enabled by default; can be disabled with `--quiet`
- `--no-warn-*`: Enabled by default; can be disabled with `--warn-*`
- `--time`: Gradle already measures the execution time of the tasks
- `--version`, `--help`, `--info`, `--unicodever`: Special actions

## Changelog

See [`CHANGELOG.md`](CHANGELOG.md).

## License

This project is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
See [`LICENSE`](LICENSE).
