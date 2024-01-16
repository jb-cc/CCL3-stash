import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.Cipher
import android.util.Base64
import javax.crypto.spec.GCMParameterSpec

object KeystoreHelper {

    private const val KEY_ALIAS = "MyKeyAlias"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"

    fun generateKey() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    fun encryptData(data: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val encryptionIv = cipher.iv
        val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))

        // Combine IV and encrypted data
        return Base64.encodeToString(encryptionIv + encryptedData, Base64.DEFAULT)
    }

    fun decryptData(encryptedData: String): String {
        val encryptedBytes = Base64.decode(encryptedData, Base64.DEFAULT)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = encryptedBytes.copyOfRange(0, 12)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))
        val decryptedBytes = cipher.doFinal(encryptedBytes, 12, encryptedBytes.size - 12)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }
}
