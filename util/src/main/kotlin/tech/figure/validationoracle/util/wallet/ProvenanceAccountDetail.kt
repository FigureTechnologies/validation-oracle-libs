package tech.figure.validationoracle.util.wallet

import com.google.common.io.BaseEncoding
import io.provenance.scope.encryption.ecies.ECUtils
import io.provenance.scope.encryption.model.DirectKeyRef
import io.provenance.scope.encryption.model.KeyRef
import io.provenance.scope.encryption.util.getAddress
import io.provenance.scope.encryption.util.toKeyPair
import tech.figure.validationoracle.util.enums.ProvenanceNetworkType
import tech.figure.validationoracle.util.extensions.base64EncodeStringAc
import tech.figure.validationoracle.util.objects.VOMetadataKeyUtil
import java.security.PrivateKey
import java.security.PublicKey

/**
 * Derives and contains all relevant key information for a given account based on various initializers, allowing a one-
 * stop shop for all account information that might be needed for provenance's various crypto utils.
 */
data class ProvenanceAccountDetail(
    val bech32Address: String,
    val publicKey: PublicKey,
    val privateKey: PrivateKey,
    val encodedPublicKey: String,
    val encodedPrivateKey: String,
    val keyRef: KeyRef,
) {
    companion object {
        fun fromJavaPrivateKey(
            privateKey: PrivateKey,
            mainNet: Boolean,
            privateKeyEncoded: String? = null,
        ): ProvenanceAccountDetail =
            privateKey.toKeyPair().let { keyPair ->
                ProvenanceAccountDetail(
                    bech32Address = keyPair.public.getAddress(mainNet),
                    publicKey = keyPair.public,
                    privateKey = keyPair.private,
                    encodedPublicKey = ECUtils.convertPublicKeyToBytes(keyPair.public).base64EncodeStringAc(),
                    encodedPrivateKey = privateKeyEncoded ?: VOMetadataKeyUtil.getBase64EncodedPrivateKey(privateKey),
                    keyRef = DirectKeyRef(keyPair),
                )
            }

        fun fromBase64PrivateKey(privateKeyEncoded: String, mainNet: Boolean): ProvenanceAccountDetail =
            fromJavaPrivateKey(
                privateKey = ECUtils.convertBytesToPrivateKey(BaseEncoding.base64().decode(privateKeyEncoded)),
                mainNet = mainNet,
                privateKeyEncoded = privateKeyEncoded,
            )

        fun fromMnemonic(
            mnemonic: String,
            networkType: ProvenanceNetworkType,
            passphrase: String = "",
        ): ProvenanceAccountDetail =
            fromBase64PrivateKey(
                privateKeyEncoded = VOMetadataKeyUtil.getBase64EncodedPrivateKey(mnemonic, networkType, passphrase),
                mainNet = networkType.isMainNet(),
            )
    }

    fun toAccountSigner(): AccountSigner = AccountSigner.fromAccountDetail(this)
}
