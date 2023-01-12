val ktlint: Configuration by configurations.creating

dependencies {
    ktlint(libs.ktlint)

    listOf(
        // Projects
        project(":util"),

        // Bundles
        libs.bundles.grpc,
        libs.bundles.jackson,
        libs.bundles.protobuf,
        libs.bundles.provenance,

        // Libraries
        libs.bouncyCastleBcProv,
        libs.kotlinLogging,
    ).forEach(::api)

    // Don't mandate a specific kotlin version.  Older versions should work fine with this lib, and
    // implementing projects will likely differ in their kotlin versions
    implementation(libs.bundles.kotlin)

    listOf(
        libs.kotlinTest,
        libs.mockk,
        libs.bundles.kotest,
        libs.bundles.testContainers,
    ).forEach(::testImplementation)
}

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintCheck by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Check Kotlin code style."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args = listOf("src/**/*.kt")
}

val ktlintFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args = listOf("-F", "src/**/*.kt")
}
