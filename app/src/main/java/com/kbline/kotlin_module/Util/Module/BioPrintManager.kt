package com.kbline.kotlin_module.Util.Module

import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.KeyPairGenerator
import java.security.Signature
import java.security.spec.ECGenParameterSpec

@RequiresApi(api = Build.VERSION_CODES.P)
class BioPrintManager(var context: Context) :
    BiometricPrompt.AuthenticationCallback(), CancellationSignal.OnCancelListener {
    var mBiometricPrompt: BiometricPrompt
    override fun onCancel() {}
    lateinit var signature: Signature
    @Throws(Exception::class)
    private fun createkey(
        keyName: String,
        invalidatedByBiometricEnrollment: Boolean
    ): KeyPairGenerator {
        val keyPairGenerator =
            KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_EC,
                "AndroidKeyStore"
            )
        val builder = KeyGenParameterSpec.Builder(
            keyName,
            KeyProperties.PURPOSE_SIGN
        )
            .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
            .setDigests(
                KeyProperties.DIGEST_SHA256,
                KeyProperties.DIGEST_SHA384,
                KeyProperties.DIGEST_SHA512
            )
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment)
        keyPairGenerator.initialize(builder.build())
        return keyPairGenerator
    }

    fun init(): Boolean {
        try {
            val keyPairGenerator = createkey("TEST", true)
            signature = Signature.getInstance("SHA256withECDSA")
            signature.initSign(keyPairGenerator.generateKeyPair().private)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun startListening() {
        if (init()) {
            cancellationSignal = CancellationSignal()
            cancellationSignal!!.setOnCancelListener(this)
            mBiometricPrompt.authenticate(
                BiometricPrompt.CryptoObject(signature!!),
                cancellationSignal!!,
                context.mainExecutor,
                this
            )
        } else {
            authenticationListener!!.failed()
        }
    }

    var cancellationSignal: CancellationSignal? = null
    fun stopListening() {
        if (cancellationSignal != null) {
            cancellationSignal!!.cancel()
            cancellationSignal = null
        }
    }

    interface AuthenticationCallback {
        fun succedded()
        fun failed()
    }

    lateinit var authenticationListener: AuthenticationCallback

    internal fun setAuthenticationListener(authenticationListener: AuthenticationCallback) {
        this.authenticationListener = authenticationListener
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        authenticationListener!!.succedded()
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        authenticationListener!!.failed()
    }

    init {
        mBiometricPrompt = BiometricPrompt.Builder(context)
            .setDescription("Description")
            .setTitle("Title")
            .setSubtitle("Subtitle")
            .setNegativeButton(
                "Cancel",
                context.mainExecutor,
                DialogInterface.OnClickListener { dialogInterface, i -> })
            .build()
    }
}