package com.kbline.kotlin_module.InstagramTag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kbline.kotlin_module.R
import com.kbline.kotlin_module.Util.KbImage
import kotlinx.android.synthetic.main.activity_main.*

class TagActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        KbImage.image("https://cdn.getyourguide.com/img/tour_img-2422815-146.jpg",instaTagView.tagImageView)

        //태그추가
        addTagBtn.setOnClickListener {
            if(tagTitleEdit.text.toString().isEmpty()) {
                Toast.makeText(applicationContext,"태그 제목을 입력해주세요.",Toast.LENGTH_SHORT).show()
            }else {
                instaTagView.addTag(10,10,tagTitleEdit.text.toString(),tagInfoEdit.text.toString())
            }

        }

        //태그 정보 가져오기
        tagInfoBtn.setOnClickListener {
            if(instaTagView.listOfTagsToBeTagged.size > 0) {
                var title = instaTagView.listOfTagsToBeTagged.get(0).title
                var info = instaTagView.listOfTagsToBeTagged.get(0).info
                var x_loc = instaTagView.listOfTagsToBeTagged.get(0).x_co_ord
                var y_loc = instaTagView.listOfTagsToBeTagged.get(0).y_co_ord

                Toast.makeText(applicationContext,"태그 제목 : ${title}\n태그 정보 : ${info}\nX_좌푯값 : ${x_loc}\nY_좌푯값 : ${y_loc}",Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(applicationContext,"등록된 태그가 없습니다.",Toast.LENGTH_SHORT).show()
            }


        }

    }
}
