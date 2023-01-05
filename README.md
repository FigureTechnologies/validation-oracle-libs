# Validation Oracle Libs
This project contains libraries for communicating with the [validation oracle (VO) smart contract](https://github.com/FigureTechnologies/validation-oracle-smart-contract).

## Status
[![stability-badge][stability-alpha]][stability-info]
[![Latest Release][release-badge]][release-latest]
[![Apache 2.0 License][license-badge]][license-url]

[![ci-status][ci-status]][ci-workflow]
[![LOC][loc-badge]][loc-report]

### Artifacts
#### Client
[![Maven Central][client-maven-badge]][client-maven-url]
#### Utilities
[![Maven Central][util-maven-badge]][util-maven-url]

[ci-status]: https://github.com/FigureTechnologies/validation-oracle-libs/actions/workflows/build.yaml/badge.svg?branch=main
[ci-workflow]: https://github.com/FigureTechnologies/validation-oracle-libs/actions/workflows/build.yaml
[client-maven-badge]: https://maven-badges.herokuapp.com/maven-central/tech.figure.validationoracle/vo-client/badge.svg?style=for-the-badge
[client-maven-url]: https://maven-badges.herokuapp.com/maven-central/tech.figure.validationoracle/vo-client
[license-badge]: https://img.shields.io/github/license/FigureTechnologies/validation-oracle-libs.svg?style=for-the-badge
[license-url]: https://github.com/FigureTechnologies/validation-oracle-libs/blob/main/LICENSE
[loc-badge]: https://tokei.rs/b1/github/FigureTechnologies/validation-oracle-libs
[loc-report]: https://github.com/FigureTechnologies/validation-oracle-libs
[release-badge]: https://img.shields.io/github/tag/FigureTechnologies/validation-oracle-libs.svg?style=for-the-badge
[release-latest]: https://github.com/FigureTechnologies/validation-oracle-libs/releases/latest
[stability-alpha]: https://img.shields.io/badge/stability-alpha-f4d03f.svg?style=for-the-badge
[stability-info]: https://github.com/mkenney/software-guides/blob/master/STABILITY-BADGES.md#alpha
[util-maven-badge]: https://maven-badges.herokuapp.com/maven-central/tech.figure.validationoracle/vo-util/badge.svg?style=for-the-badge
[util-maven-url]: https://maven-badges.herokuapp.com/maven-central/tech.figure.validationoracle/vo-util

## Compatibility

| [Kotlin Client](https://github.com/FigureTechnologies/validation-oracle-libs/releases) | [VO Smart Contract](https://github.com/FigureTechnologies/validation-oracle-smart-contract/releases) |
|----------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------|
| TBA                                                                                    | TBA                                                                                                  |

## Using the VOClient
### Creating an VOClient instance
To establish an [VOClient](client/src/main/kotlin/tech/figure/validationoracle/client/base/VOClient.kt), first,
create a [PbClient](https://github.com/provenance-io/pb-grpc-client-kotlin/blob/main/src/main/kotlin/io/provenance/client/grpc/PbClient.kt). 
The `PbClient` comes pre-bundled with the client artifact, when imported.  The `PbClient` controls which provenance 
instance the application is communicating with, and, importantly, the provenance instance to which the Validation
Oracle smart contract is deployed.  Then, with the `PbClient` instance, create your `VOClient`.

#### Example:

```kotlin
import io.provenance.client.grpc.GasEstimationMethod
import io.provenance.client.grpc.PbClient
import java.net.URI
import tech.figure.validationoracle.client.client.base.VOClient
import tech.figure.validationoracle.client.client.base.ContractIdentifier

class SampleConfiguration {
  fun buildClients() {
    // First, you'll need a PbClient
    val pbClient = PbClient(
      // chain-local for a local environment, pio-testnet-1 for the test environment
      chainId = "my-chain-id",
      // http://localhost:9090 for local, or some non-local channel uri
      channelUri = URI("my-channel-uri"),
      // or GasEstimationMethod.COSMOS_SIMULATION
      gasEstimationMethod = GasEstimationMethod.MSG_FEE_CALCULATION
    )
    // Then, the VOClient will know where to look for the validation oracle smart contract
    // The root interfaces are exposed if you want to create your own implementation, but a default implementation can
    // easily be built simply by using the default function in the companion object of the VOClient interface:
    val voClient = VOClient.getDefault(
      // validationoracle.pb for local, or some other contract name. 
      // Alternatively, if the contract's bech32 address is directly known, you can use ContractIdentifier.Address("mycontractaddressbech32")
      contractIdentifier = ContractIdentifier.Name("mycontractname"),
      pbClient = pbClient,
      // This is the default and can be omitted, but it exists for if you'd like to provide your own Jackson ObjectMapper instance
      objectMapper = VOObjectMapperUtil.OBJECT_MAPPER,
    )
  }
}
```
