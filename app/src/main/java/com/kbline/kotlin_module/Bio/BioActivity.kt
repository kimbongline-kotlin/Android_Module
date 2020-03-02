package com.kbline.kotlin_module.Bio

import android.hardware.biometrics.BiometricManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kbline.kotlin_module.R
import com.kbline.kotlin_module.Util.Module.BioPrintManager
import com.kbline.kotlin_module.Util.Module.BiometricUtilities
import com.kbline.kotlin_module.Util.Module.FingerPrintMannager
import kotlinx.android.synthetic.main.activity_bio.*

class BioActivity : AppCompatActivity() {

    lateinit var manager : FingerPrintMannager
    lateinit var bioManager : BioPrintManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bio)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager = FingerPrintMannager(applicationContext)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            bioManager = BioPrintManager(applicationContext)
        }

        bioAutBtn.setOnClickListener {

            if(Build.VERSION.SDK_INT  > Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
                manager.startListening();
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                bioManager.setAuthenticationListener(object : BioPrintManager.AuthenticationCallback {
                    override fun succedded() {

                    }

                    override fun failed() {


                    }
                })


                Log.d("object",BiometricUtilities.isHardwareSupported(applicationContext).toString())//하드웨어 지원여부
                Log.d("object",BiometricUtilities.isSdkVersionSupported().toString()) //SDK지원 여부
                Log.d("object",BiometricUtilities.isBiometricPromptEnabled().toString()) //바이오 인증 지원 여부
                Log.d("object",BiometricUtilities.isFingerprintAvailable(applicationContext).toString()) //지문인식 지원 여
              //  bioManager.startListening();
            }

        }
    }
}
