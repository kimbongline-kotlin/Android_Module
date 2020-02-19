package com.kbline.kotlin_module.FirebaseDynamic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kbline.kotlin_module.R
import com.kbline.kotlin_module.Util.ClipboardUtil
import com.kbline.kotlin_module.Util.FirebaseShare
import com.kbline.kotlin_module.Util.ShareReturn
import kotlinx.android.synthetic.main.activity_dynamic.*

class DynamicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic)

        createBtn.setOnClickListener {
            FirebaseShare.getDynaicUrl(
                "https://www.naver.com", // targetUrl ex)www.naver.com other...
                "kbline.page.link" , //
                titleEdit.text.toString(), //title
                infoEdit.text.toString(),  //description
                otherEdit.text.toString(), //url
                "https://cdn.getyourguide.com/img/tour_img-2422815-146.jpg", //image_url

                object : ShareReturn {
                override fun onError(t: Throwable) {

                    Log.d("object",t.localizedMessage)
                }

                override fun onSuccess(url: String) {

                    urlEdit.setText(url)

                }
            })
        }

        copyBtn.setOnClickListener {
            ClipboardUtil.copy(applicationContext,urlEdit.text.toString(),"'${urlEdit.text.toString()}' 이 복사되었습니다.")
        }
    }
}
