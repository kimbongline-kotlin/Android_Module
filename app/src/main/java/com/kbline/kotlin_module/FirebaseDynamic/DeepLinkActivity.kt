package com.kbline.kotlin_module.FirebaseDynamic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kbline.kotlin_module.R

class DeepLinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deep_link)


        if(intent.action == Intent.ACTION_VIEW) {
            Log.d("object",intent.data.toString()) //Url 데이터 가져오기

        }


    }
}
