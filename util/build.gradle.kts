dependencies {
    listOf(
        libs.bundles.jackson,
        libs.bundles.provenance,
        libs.bouncyCastleBcProv,
    ).forEach(::api)

    listOf(
        libs.kotlinTest,
        libs.mockk,
    ).forEach(::testImplementation)
}
