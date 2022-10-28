rootProject.name = "validation-oracle-libs"

include("util", "client")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

gradle.rootProject {
    val libraryVersion = rootProject.property("libraryVersion") ?: error("Missing libraryVersion - check gradle.properties")
    allprojects {
        group = "tech.figure.validationoracle"
        version = libraryVersion
        description = "Various tools for interacting with the validation oracle smart contract"
    }
}
