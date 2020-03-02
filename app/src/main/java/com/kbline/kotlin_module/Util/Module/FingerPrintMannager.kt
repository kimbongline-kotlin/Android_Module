package com.kbline.kotlin_module.Util.Module

import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.annotation.RequiresApi
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

@RequiresApi(api = Build.VERSION_CODES.M)
class FingerPrintMannager(context: Context) :
    FingerprintManager.AuthenticationCallback(), CancellationSignal.OnCancelListener {
    override fun onCancel() {}
    var fingerprintManager: FingerprintManager
    val isFingerprintAuthAvailable: Boolean
        get() = fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()

    @Throws(Exception::class)
    private fun createkey(
        keyName: String,
        invalidatedByBiometricEnrollment: Boolean
    ): KeyGenerator {
        val mKeyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )
        val builder = KeyGenParameterSpec.Builder(
            keyName,
            KeyProperties.PURPOSE_ENCRYPT or
                    KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setUserAuthenticationRequired(true)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment)
        }
        mKeyGenerator.init(builder.build())
        mKeyGenerator.generateKey()
        return mKeyGenerator
    }

    lateinit var cipher: Cipher
    fun init(): Boolean {
        try {
            val keyGenerator = createkey("TEST", true)
            cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
            cipher.init(Cipher.ENCRYPT_MODE, keyGenerator.generateKey())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    var cancellationSignal: CancellationSignal? = null
    fun startListening() {
        if (!isFingerprintAuthAvailable) {
            return
        }
        if (init()) {
            cancellationSignal = CancellationSignal()
            cancellationSignal!!.setOnCancelListener(this)
            fingerprintManager.authenticate(
                FingerprintManager.CryptoObject(cipher!!),
                cancellationSignal,
                0,
                this,
                null
            )
        } else {
            Log.d("object", "error")
        }
    }

    fun stopListening() {
        if (cancellationSignal != null) {
            cancellationSignal!!.cancel()
            cancellationSignal = null
        }
    }

    interface AuthenticationListener {
        fun succedded()
        fun failed()
    }

    lateinit var authenticationListener: AuthenticationListener
    internal fun setAuthenticationListener(authenticationListener: AuthenticationListener) {
        this.authenticationListener = authenticationListener
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        authenticationListener!!.succedded()
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        authenticationListener!!.failed()
    }

    init {
        fingerprintManager =
            context.getSystemService(FingerprintManager::class.java)
    }
}