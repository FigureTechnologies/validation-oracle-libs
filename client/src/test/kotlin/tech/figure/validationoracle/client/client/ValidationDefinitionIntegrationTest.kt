package tech.figure.validationoracle.client.client

import io.kotest.core.spec.Order
import io.provenance.client.grpc.GasEstimationMethod
import io.provenance.client.grpc.PbClient
import tech.figure.validationoracle.client.client.base.ContractIdentifier
import tech.figure.validationoracle.client.client.base.VOClient
import tech.figure.validationoracle.client.test.IntegrationTestBase
import java.net.URI

@Order(2)
class ValidationDefinitionIntegrationTest : IntegrationTestBase({
    val pbClient = PbClient(
        chainId = "chain-local",
        channelUri = URI("http://localhost:9090"),
        gasEstimationMethod = GasEstimationMethod.MSG_FEE_CALCULATION,
    )
    autoClose(pbClient)
    val voClient = VOClient.getDefault(
        ContractIdentifier.Name("vo.sc.pb"),
        pbClient,
    )
    "Creating a new validation definition" xshould {
        "fail on an invalid new admin address" {
            // TODO: Implement
        }
        "succeed with a valid request body" {
            // TODO: Implement
        }
    }
}) {
    override val specTestContainers: List<SpecTestContainer> = listOf(
        SpecTestContainer.PROVENANCE,
    )
}
