package com.kbline.kotlin_module

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.kbline.kotlin_module.FirebaseDynamic.DynamicActivity
import com.kbline.kotlin_module.GalleryUtil.KbPickerActivity
import com.kbline.kotlin_module.GalleryUtil.KbPickerCall
import com.kbline.kotlin_module.GalleryUtil.Util.CameraValue
import com.kbline.kotlin_module.InstagramTag.TagActivity
import com.kbline.kotlin_module.MVVM.GiphyActivity
import com.kbline.kotlin_module.Util.ClipboardUtil
import kotlinx.android.synthetic.main.activity_dynamic.*
import kotlinx.android.synthetic.main.activity_home.*
import java.util.ArrayList

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        tagBtn.setOnClickListener {
            startActivity(Intent(applicationContext,TagActivity::class.java))
        }
        pickerBtn.setOnClickListener {


            TedPermission.with(this)
                .setPermissionListener(object: PermissionListener {
                    override fun onPermissionGranted() {


                        //CameraValue.TYPE = 0 : 한장, 1 여러장 선택
                        //CameraValue.MAX_VALUE = 최대 선택 가능한 장수, 5
                        //CameraValue.RESULT = 이미지 리턴 값.


                        KbPickerCall.open(this@HomeActivity,applicationContext,CameraValue.MULTI,10)


                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                        //SnackShow("권한 설정 후 이용해주세요.")
                    }


                })
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA)
                .check()
        }

        HashBtn.setOnClickListener {
            startActivity(Intent(applicationContext,HashTagActivity::class.java))
        }

        dynamicBtn.setOnClickListener {
            startActivity(Intent(applicationContext,DynamicActivity::class.java))
        }

        mvvmBtn.setOnClickListener {
            startActivity(Intent(applicationContext,GiphyActivity::class.java))
        }


        if(intent.action == Intent.ACTION_VIEW) {
            check_dynamic_link()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CameraValue.RESULT && resultCode == Activity.RESULT_OK) {
            Log.d("IMAGE_URL",data!!.getStringArrayListExtra(CameraValue.IMAGES).size.toString())
        }
    }

    fun check_dynamic_link() {

        FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener {
            if(it != null) {
                var url = it.link
                var return_data = url.getQueryParameter("item")
                dynamic_return_label.setText(return_data.toString())
            }
        }
    }
}
