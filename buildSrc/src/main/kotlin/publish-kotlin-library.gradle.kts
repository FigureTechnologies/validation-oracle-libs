import org.gradle.api.publish.maven.MavenPublication

plugins {
    `maven-publish`
    `java-library`
    signing
    id("io.github.gradle-nexus.publish-plugin")
}

val projectGroup = rootProject.group
val projectVersion = project.property("version")?.takeIf { it != "unspecified" }?.toString() ?: "1.0-SNAPSHOT"

configure<io.github.gradlenexus.publishplugin.NexusPublishExtension> {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(findProject("ossrhUsername")?.toString() ?: System.getenv("OSSRH_USERNAME"))
            password.set(findProject("ossrhPassword")?.toString() ?: System.getenv("OSSRH_PASSWORD"))
            stagingProfileId.set("858b6e4de4734a") // tech.figure staging profile id
        }
    }
}

subprojects {
    apply {
        plugin("maven-publish")
        plugin("kotlin")
        plugin("java-library")
        plugin("signing")
        plugin("core-config")
    }

    java {
        withSourcesJar()
        withJavadocJar()
    }

    // Add a "vo-" prefix to each library's name.  This will prevent jar collisions with other libraries that have
    // ambiguously-named resources like this one, eg: client-1.0.0.jar == bad
    val artifactName = "vo-$name"
    val artifactVersion = projectVersion.toString()

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("maven") {
                groupId = projectGroup.toString()
                artifactId = artifactName
                version = artifactVersion

                from(components["java"])

                pom {
                    name.set("Validation Oracle Kotlin Libraries")
                    description.set("Various tools for interacting with the Validation Oracle smart contract")
                    url.set("https://figure.tech")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("jchickanosky")
                            name.set("Joe Chickanosky")
                            email.set("jchickanosky@figure.com")
                        }
                        developer {
                            id.set("rpatel-figure")
                            name.set("Ravi Patel")
                            email.set("rpatel@figure.com")
                        }
                    }

                    scm {
                        developerConnection.set("git@github.com:FigureTechnologies/validation-oracle-libs.git")
                        connection.set("https://github.com/FigureTechnologies/validation-oracle-libs.git")
                        url.set("https://github.com/FigureTechnologies/validation-oracle-libs")
                    }
                }
            }
        }
        if (!System.getenv("DISABLE_SIGNING").toBoolean()) {
            configure<SigningExtension> {
                sign(publications["maven"])
            }
        }
    }
}
