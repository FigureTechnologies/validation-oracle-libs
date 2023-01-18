package tech.figure.validationoracle.client.test

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.WordSpec
import io.provenance.client.grpc.GasEstimationMethod
import io.provenance.client.grpc.PbClient
import io.provenance.client.grpc.Signer
import mu.KLogger
import mu.KotlinLogging
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.testcontainers.containers.Container.ExecResult
import org.testcontainers.containers.ContainerState
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitStrategy
import tech.figure.validationoracle.client.client.base.ContractIdentifier
import tech.figure.validationoracle.client.client.base.VOClient
import tech.figure.validationoracle.util.enums.ProvenanceNetworkType
import tech.figure.validationoracle.util.wallet.AccountSigner
import java.io.File
import java.net.URI
import java.security.Security
import java.time.Duration

private const val ResourceDirectory = "src/test/resources"
// private const val MAXIMUM_SETUP_SCRIPT_WAIT_TIME_IN_SECONDS = 20L

val log: KLogger = KotlinLogging.logger("IntegrationTestBaseClass") // TODO: Needs SL4J provider to actually work

abstract class IntegrationTestBase(
    body: WordSpec.() -> Unit,
) : WordSpec(body) {
    init {
        Security.addProvider(BouncyCastleProvider())
    }

    companion object {
        internal class KDockerComposeContainer(file: File) : DockerComposeContainer<KDockerComposeContainer>(file)

        private const val CONTRACT_ADMIN_MNEMONIC = "mouse dove lava deputy east toddler long round person margin " +
            "ecology auto fiber drink put buzz lesson van popular autumn marble flame prepare area"
        private const val PARTY1_MNEMONIC = "nature crowd safe rifle amateur vapor dismiss inquiry flag shadow quiz " +
            "fuel divide donate major fetch leader valve fence across fringe orbit poet evidence"
        private const val PARTY2_MNEMONIC = "demise pyramid jelly early draw audit mom name divide dynamic beef way " +
            "pond fence train spare quality kangaroo vibrant portion distance debris increase acoustic"
        private const val PARTY3_MNEMONIC = "exact hidden thought shiver accuse copy supreme umbrella tell tiger " +
            "during weird region lawn patrol balance lunar wheel much aware grit script dutch sauce"

        @Suppress("ktlint:no-multi-spaces")
        private val defaultDockerComposeEnvironment: Map<String, String> = configurableEnvironment(
            /** Images */
            // TODO: Take versions as inputs in GitHub job, and output them in job's output Markdown
            "PROVENANCE_VERSION"      to "v1.13.1",
            "RUST_OPTIMIZER_VERSION"  to "0.12.11",
            "UBUNTU_VERSION"          to "22.04",
            /** Variables */
            "CONTRACT_ADMIN_MNEMONIC" to CONTRACT_ADMIN_MNEMONIC,
            "PARTY1_MNEMONIC"         to PARTY1_MNEMONIC,
            "PARTY2_MNEMONIC"         to PARTY2_MNEMONIC,
            "PARTY3_MNEMONIC"         to PARTY3_MNEMONIC,
        )

        protected val dockerComposeEnvironmentOverrides: Map<String, String> = emptyMap()

        internal val instance: KDockerComposeContainer by lazy {
            KDockerComposeContainer(File("$ResourceDirectory/docker-compose.yml")).apply {
                withEnv(defaultDockerComposeEnvironment + dockerComposeEnvironmentOverrides)
                withRemoveImages(DockerComposeContainer.RemoveImages.ALL)
                withLocalCompose(
                    getEnvOr("USE_LOCAL_COMPOSE_FOR_TESTCONTAINERS", "true").toBoolean().also {
                        log.info("Local Docker Compose for tests = $it")
                    },
                )
            }
        }

        protected fun configurableEnvironment(vararg envVars: Pair<String, String>): Map<String, String> =
            envVars.associate { (envVar, default) ->
                envVar to getEnvOr(envVar, default)
            }

        private fun getEnvOr(value: String, default: String) =
            System.getenv(value)?.takeIf { it.isNotBlank() } ?: default

        @JvmStatic
        protected fun WordSpec.getPbClient(): PbClient = PbClient(
            chainId = "chain-local",
            channelUri = URI("http://localhost:9090"),
            gasEstimationMethod = GasEstimationMethod.MSG_FEE_CALCULATION,
        ).also { pbClient ->
            autoClose(pbClient)
        }

        @JvmStatic
        protected fun WordSpec.getVoClient(pbClient: PbClient = getPbClient()): VOClient = VOClient.getDefault(
            ContractIdentifier.Name("vo.sc.pb"),
            pbClient,
        )
    }

    sealed class SpecTestContainer(
        val serviceName: String,
        val waitStrategy: WaitStrategy = Wait.defaultWaitStrategy(),
    ) {
        object Provenance : SpecTestContainer(
            "provenance",
            Wait.forLogMessage(
                ".*The contract was successfully instantiated to the local Provenance instance at address tp\\w+\\n.*",
                1,
            ).apply {
                withStartupTimeout(Duration.ofMinutes(5L))
            },
        ) {
            private fun getAccountFromMnemomic(mnemonic: String): AccountSigner =
                AccountSigner.fromMnemonic(mnemonic, ProvenanceNetworkType.TESTNET)

            val contractAdminAccount: Signer by lazy { getAccountFromMnemomic(CONTRACT_ADMIN_MNEMONIC) }
            val party1Account: Signer by lazy { getAccountFromMnemomic(PARTY1_MNEMONIC) }
            val party2Account: Signer by lazy { getAccountFromMnemomic(PARTY2_MNEMONIC) }
            val party3Account: Signer by lazy { getAccountFromMnemomic(PARTY3_MNEMONIC) }
        }

        private val container: ContainerState by lazy {
            instance.getContainer(serviceName).get()
        }

        protected fun runCommand(input: String): ExecResult =
            container.execInContainer(*input.split(" ").toTypedArray())

        protected fun runBash(input: String): ExecResult =
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
    protected val specTestContainers: List<SpecTestContainer> = listOf(SpecTestContainer.Provenance)
}
