package tech.figure.validationoracle.client.client

import cosmwasm.wasm.v1.QueryOuterClass
import io.mockk.MockKStubScope
import io.mockk.every
import io.mockk.mockk
import io.provenance.client.grpc.PbClient
import io.provenance.client.protobuf.extensions.queryWasm
import io.provenance.name.v1.QueryResolveResponse
import io.provenance.scope.util.toByteString
import org.junit.jupiter.api.Test
import tech.figure.validationoracle.client.client.base.ContractIdentifier
import tech.figure.validationoracle.client.client.base.VOQuerier
import tech.figure.validationoracle.client.client.impl.DefaultVOQuerier
import tech.figure.validationoracle.client.domain.model.EntityDetail
import tech.figure.validationoracle.client.domain.model.ValidationCost
import tech.figure.validationoracle.client.domain.model.ValidationDefinition
import tech.figure.validationoracle.client.domain.model.ValidationRequestOrder
import tech.figure.validationoracle.client.domain.model.ValidatorConfiguration
import tech.figure.validationoracle.client.domain.query.QueryValidationDefinitionByType
import tech.figure.validationoracle.client.domain.query.QueryValidationRequestById
import tech.figure.validationoracle.client.helper.OBJECT_MAPPER
import tech.figure.validationoracle.client.helper.assertSucceeds
import tech.figure.validationoracle.client.helper.toJsonPayload
import java.math.BigInteger
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultVOQuerierTest {

    companion object {
        val TEST_VALIDATION_TYPE = "MyValType"
        val TEST_VALIDATOR_DISPLAY_NAME = "data"
        val TEST_REQUET_ID = "12345"
    }

    @Test
    fun `test queryValidationDefinitionByValidationType`() {
        val suite = MockSuite.new()
        suite.mockQueryReturns(mockVODefinition())
        val validationDefinitionFromQuery = assertSucceeds("Expected the query to execute successfully when the proper response is returned") {
            suite.querier.queryValidationDefinitionByType(QueryValidationDefinitionByType(TEST_VALIDATION_TYPE))
        }
        assertEquals(TEST_VALIDATOR_DISPLAY_NAME, validationDefinitionFromQuery?.displayName)
        assertEquals(TEST_VALIDATION_TYPE, validationDefinitionFromQuery?.validationType)
    }

    @Test
    fun `test queryValidationRequestById`() {
        val suite = MockSuite.new()
        suite.mockQueryNullResponse()
        assertNull(suite.querier.queryValidationRequestById(QueryValidationRequestById("TestIdNotFound")))
        suite.mockQueryReturns(mockValidationRequestOrder())
        val validationRequestOrderFromQuery = assertSucceeds("Expected the query to execute successfully when the proper response is returned") {
            suite.querier.queryValidationRequestById(QueryValidationRequestById(TEST_REQUET_ID))
        }
        assertEquals(TEST_REQUET_ID, validationRequestOrderFromQuery?.id)
    }

    fun mockVODefinition(validationType: String = TEST_VALIDATION_TYPE): ValidationDefinition = ValidationDefinition(
        validationType = validationType,
        displayName = TEST_VALIDATOR_DISPLAY_NAME,
        validators = listOf(
            mockValidator("Validator1")
        ),
        enabled = true,
    )

    fun mockEntityDetail(): EntityDetail = EntityDetail(
        address = "addresss",
        name = "name",
        description = "descr",
        homeUrl = "home URL",
        sourceUrl = "source URL",
    )

    fun mockValidator(validationType: String): ValidatorConfiguration = ValidatorConfiguration(
        validationCosts = listOf(ValidationCost(
            amount = BigInteger.valueOf(123),
            denom = "nhash",
            destination = mockEntityDetail(),
        )),
        validationType = validationType,
        validator = mockEntityDetail(),
    )

    fun mockValidationRequestOrder(id: String = TEST_REQUET_ID): ValidationRequestOrder = ValidationRequestOrder(
         id = id
    )

    private data class MockSuite(
        val querier: VOQuerier,
        val pbClient: PbClient,
    ) {
        companion object {
            const val DEFAULT_CONTRACT_NAME = "testvalidations.pb"
            const val DEFAULT_CONTRACT_ADDRESS = "tp14hj2tavq8fpesdwxxcu44rty3hh90vhujrvcmstl4zr3txmfvw9s96lrg8"

            fun new(contractName: String = DEFAULT_CONTRACT_NAME): MockSuite {
                val pbClient = mockk<PbClient>()
                every { pbClient.nameClient.resolve(any()) } returns QueryResolveResponse.newBuilder().setAddress(
                    DEFAULT_CONTRACT_ADDRESS
                ).build()
                return MockSuite(
                    querier = DefaultVOQuerier(
                        contractIdentifier = ContractIdentifier.Address(DEFAULT_CONTRACT_ADDRESS),
                        objectMapper = OBJECT_MAPPER,
                        pbClient = pbClient,
                    ),
                    pbClient = pbClient,
                )
            }
        }

        fun <T : Any> mockQueryReturns(value: T) {
            mockQuery {
                this returns QueryOuterClass.QuerySmartContractStateResponse.newBuilder().setData(value.toJsonPayload())
                    .build()
            }
        }

        fun mockQueryNullResponse() {
            mockQuery { this returns getNullContractResponse() }
        }

        fun getNullContractResponse(): QueryOuterClass.QuerySmartContractStateResponse = QueryOuterClass.QuerySmartContractStateResponse
            .newBuilder()
            .setData("null".toByteString())
            .build()

        private fun mockQuery(
            queryFn: MockKStubScope<QueryOuterClass.QuerySmartContractStateResponse, QueryOuterClass.QuerySmartContractStateResponse>.() -> Unit,
        ) {
            every { pbClient.wasmClient.queryWasm(any()) }.queryFn()
        }
    }
}
