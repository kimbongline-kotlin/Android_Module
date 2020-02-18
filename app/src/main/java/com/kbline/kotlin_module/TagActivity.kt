package com.kbline.kotlin_module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.kbline.kotlin_module.InstagramTagItem.InstaTag
import com.kbline.kotlin_module.Util.KbImage
import kotlinx.android.synthetic.main.activity_main.*

class TagActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        KbImage.image("https://cdn.getyourguide.com/img/tour_img-2422815-146.jpg",instaTagView.tagImageView)

        addTagBtn.setOnClickListener {
            if(tagTitleEdit.text.toString().isEmpty()) {
                Toast.makeText(applicationContext,"태그 제목을 입력해주세요.",Toast.LENGTH_SHORT).show()
            }else {
                instaTagView.addTag(10,10,tagTitleEdit.text.toString(),tagInfoEdit.text.toString())
            }

        }

        tagInfoBtn.setOnClickListener {
            if(instaTagView.listOfTagsToBeTagged.size > 0) {
                var title = instaTagView.listOfTagsToBeTagged.get(0).title
                var info = instaTagView.listOfTagsToBeTagged.get(0).info
                var x_loc = instaTagView.listOfTagsToBeTagged.get(0).x_co_ord
                var y_loc = instaTagView.listOfTagsToBeTagged.get(0).y_co_ord
            }else {
                Toast.makeText(applicationContext,"등록된 태그가 없습니다.",Toast.LENGTH_SHORT).show()
            }


        }

    }
}
