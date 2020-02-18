package com.kbline.kotlin_module

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.kbline.kotlin_module.GalleryUtil.KbPickerActivity
import com.kbline.kotlin_module.GalleryUtil.Util.CameraValue
import kotlinx.android.synthetic.main.activity_home.*
import java.util.ArrayList

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        pickerBtn.setOnClickListener {


            TedPermission.with(this)
                .setPermissionListener(object: PermissionListener {
                    override fun onPermissionGranted() {

                        startActivityForResult(Intent(applicationContext,KbPickerActivity::class.java)
                            .putExtra(CameraValue.TYPE,CameraValue.SINGLE)
                            .putExtra(CameraValue.MAX_VALUE,5)
                            ,CameraValue.RESULT)


                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                        //SnackShow("권한 설정 후 이용해주세요.")
                    }


                })
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA) // 확인할 권한을 다중 인자로 넣어줍니다.
                .check()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CameraValue.RESULT && resultCode == Activity.RESULT_OK) {
            Log.d("IMAGE_URL",data!!.getStringArrayListExtra(CameraValue.IMAGES).size.toString())
        }
    }
}
