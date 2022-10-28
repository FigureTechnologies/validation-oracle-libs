package tech.figure.validationoracle.util.objects

import io.provenance.hdwallet.bip39.MnemonicWords
import io.provenance.hdwallet.ec.extensions.toJavaECPrivateKey
import io.provenance.hdwallet.wallet.Wallet
import io.provenance.scope.encryption.ecies.ECUtils
import tech.figure.validationoracle.util.enums.ProvenanceNetworkType
import tech.figure.validationoracle.util.extensions.base64EncodeStringAc
import java.security.PrivateKey

object VOMetadataKeyUtil {
    fun getBase64EncodedPrivateKey(
        mnemonic: String,
        networkType: ProvenanceNetworkType = ProvenanceNetworkType.TESTNET,
        passphrase: String = "",
    ): String =
        Wallet.fromMnemonic(
            hrp = networkType.prefix,
            passphrase = passphrase,
            mnemonicWords = MnemonicWords.of(mnemonic),
            testnet = networkType.isTestNet(),
        )[networkType.hdPath]
            .keyPair
            .privateKey
            .let(VOMetadataKeyUtil::getBase64EncodedPrivateKey)

    fun getBase64EncodedPrivateKey(
        privateKey: io.provenance.hdwallet.ec.PrivateKey
    ): String = getBase64EncodedPrivateKey(privateKey.toJavaECPrivateKey())

    fun getBase64EncodedPrivateKey(
        privateKey: PrivateKey,
    ): String = ECUtils.convertPrivateKeyToBytes(privateKey).base64EncodeStringAc()
}
