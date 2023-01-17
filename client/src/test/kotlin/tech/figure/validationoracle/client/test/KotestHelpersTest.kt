package tech.figure.validationoracle.client.test

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import java.util.UUID as JavaUUID

class KotestHelpersTest : WordSpec({
    "JavaUUID.toBech32Address" should {
        "convert UUIDs to the appropriate bech32 scope address" {
            JavaUUID.fromString("a5e5a828-9a48-11ec-8193-1731fd63d6a6").toBech32ProvenanceAddress("scope") shouldBe
                "scope1qzj7t2pgnfyprmypjvtnrltr66nqd4c3cq"
            JavaUUID.fromString("b0e40e77-3d13-4bed-9464-09cd9512d301").toBech32ProvenanceAddress("scope") shouldBe
                "scope1qzcwgrnh85f5hmv5vsyum9gj6vqs754j36"
        }
    }
})
