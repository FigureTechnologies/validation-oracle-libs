package tech.figure.validationoracle.client.test

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.WordSpec
import mu.KLogger
import mu.KotlinLogging
import org.testcontainers.containers.Container
import org.testcontainers.containers.ContainerState
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitStrategy
import java.io.File
import java.time.Duration

private const val ResourceDirectory = "src/test/resources"
// private const val MAXIMUM_SETUP_SCRIPT_WAIT_TIME_IN_SECONDS = 20L

val logger: KLogger = KotlinLogging.logger("IntegrationTestBaseClass")

abstract class IntegrationTestBase(
    body: WordSpec.() -> Unit,
) : WordSpec(body) {
    protected open val log: KLogger = logger

    companion object {
        internal class KDockerComposeContainer(file: File) : DockerComposeContainer<KDockerComposeContainer>(file)

        @Suppress("ktlint:no-multi-spaces")
        private val defaultDockerComposeEnvironment: Map<String, String> = configurableEnvironment(
            /** Images */
            // TODO: Take versions as inputs in GitHub job, and output them in job's output Markdown
            "PROVENANCE_VERSION"         to "v1.13.1",
            "RUST_OPTIMIZER_VERSION"     to "0.12.11",
            "UBUNTU_VERSION"             to "22.04",
            /** External ports (currently none needed) */
        )

        protected val dockerComposeEnvironmentOverrides: Map<String, String> = emptyMap()

        internal val instance: KDockerComposeContainer by lazy {
            KDockerComposeContainer(File("$ResourceDirectory/docker-compose.yml")).apply {
                withEnv(defaultDockerComposeEnvironment + dockerComposeEnvironmentOverrides)
                withRemoveImages(DockerComposeContainer.RemoveImages.ALL)
                withLocalCompose(true) // TODO: Make configurable in GitHub Actions?
            }
        }

        private fun configurableEnvironment(vararg envVars: Pair<String, String>): Map<String, String> =
            envVars.associate { (envVar, value) ->
                envVar to (System.getenv(envVar)?.takeIf { it.isNotBlank() } ?: value)
            }
    }

    enum class SpecTestContainer( // TODO: Possibly change into a sealed class instead
        val serviceName: String,
        val waitStrategy: WaitStrategy = Wait.defaultWaitStrategy(),
    ) {
        PROVENANCE(
            "provenance",
            Wait.forLogMessage(
                ".*The contract was successfully instantiated to the local Provenance instance at address tp\\w+\\n.*",
                1,
            ).apply {
                withStartupTimeout(Duration.ofMinutes(5L))
            },
        ),
        ;

        private val container: ContainerState by lazy {
            instance.getContainer(serviceName).get()
        }

        fun runCommand(input: String): Container.ExecResult =
            container.execInContainer(*input.split(" ").toTypedArray())

        fun runBash(input: String): Container.ExecResult =
            container.execInContainer("bash", "-c", *input.split(" ").toTypedArray())

        /**
         * Transforms a service's defined name. Derived from
         * [this issue comment](https://github.com/testcontainers/testcontainers-java/issues/4281#issuecomment-1164145098).
         */
        private fun KDockerComposeContainer.getContainer(serviceName: String) =
            if (!serviceName.matches(Regex(".*_[0-9]+"))) {
                serviceName + "_1"
            } else {
                serviceName
            }.let { serviceInstanceName ->
                getContainerByServiceName(serviceInstanceName)
            }
    }

    override fun extensions(): List<Extension> = listOf()

    /**
     * A list of the containers which are invoked in the test body.
     */
    protected abstract val specTestContainers: List<SpecTestContainer>
}
