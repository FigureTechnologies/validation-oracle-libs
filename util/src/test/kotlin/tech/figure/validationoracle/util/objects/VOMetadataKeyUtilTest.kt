package tech.figure.validationoracle.util.objects

import io.provenance.client.wallet.fromMnemonic
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.figure.validationoracle.util.enums.ProvenanceNetworkType
import tech.figure.validationoracle.util.wallet.ProvenanceAccountDetail
import java.security.Security
import kotlin.test.assertEquals

class VOMetadataKeyUtilTest {
    @BeforeEach
    fun setupTest() {
        // The BouncyCastle provider must be registered for the KeyUtil to properly utilize it when leveraging
        // Provenance resources that use it
        Security.addProvider(BouncyCastleProvider())
    }

    @Test
    fun `test util produces usable output`() {
        val mnemonic = "surge update round quantum script shed tissue maple minimum raw movie below prevent appear dice bullet pyramid tragic glue globe egg object era safe"
        // This uses wallet signer to verify that the correct address will be generated
        val addressFromWalletSigner = fromMnemonic(
            networkType = ProvenanceNetworkType.TESTNET.toNetworkType(),
            mnemonic = mnemonic,
            isMainNet = false,
        ).address()
        val privateKeyEncoded = VOMetadataKeyUtil.getBase64EncodedPrivateKey(
            mnemonic = mnemonic,
            networkType = ProvenanceNetworkType.TESTNET,
        )
        val addressFromAccountDetail = ProvenanceAccountDetail.fromBase64PrivateKey(privateKeyEncoded, mainNet = false).bech32Address
        assertEquals(
            expected = addressFromWalletSigner,
            actual = addressFromAccountDetail,
            message = "The WalletSigner-derived address should match the address from ProvenanceAccountDetail after decoding the produced base64 encoded private key",
        )
    }
}
