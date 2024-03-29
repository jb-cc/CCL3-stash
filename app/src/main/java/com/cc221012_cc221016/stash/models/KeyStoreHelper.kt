import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.Cipher
import android.util.Base64
import android.util.Log
import javax.crypto.spec.GCMParameterSpec

object KeystoreHelper {

    private const val KEY_ALIAS = "MyKeyAlias"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"

    fun generateKey() {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
        Log.d("KeystoreHelper", "Key generated or already exists")
    }


    fun encryptData(data: String): String {
        try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            val encryptionIv = cipher.iv
            val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
            Log.d("KeystoreHelper", "Encrypted data")
            // Combine IV and encrypted data
            return Base64.encodeToString(encryptionIv + encryptedData, Base64.DEFAULT)
        } catch (e: Exception) {
            throw RuntimeException("Failed to encrypt data", e)
        }
    }

    fun decryptData(encryptedData: String): String {
        try {
            val encryptedBytes = Base64.decode(encryptedData, Base64.DEFAULT)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val iv = encryptedBytes.copyOfRange(0, 12)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))
            val decryptedBytes = cipher.doFinal(encryptedBytes, 12, encryptedBytes.size - 12)
            Log.d("KeystoreHelper", "Decrypted data")
            return String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            throw RuntimeException("Failed to decrypt data", e)
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        Log.d("KeystoreHelper", "Key generated or already exists")
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey

    }

}
