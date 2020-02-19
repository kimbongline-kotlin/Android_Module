package com.kbline.kotlin_module.RealTimeChat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.jakewharton.rxbinding2.widget.RxTextView
import com.kbline.kotlin_module.KbActivity
import com.kbline.kotlin_module.MVVM.Adapter.TrendAdapter
import com.kbline.kotlin_module.R
import com.kbline.kotlin_module.RealTimeChat.Adapter.MessageAdapter
import com.kbline.kotlin_module.RealTimeChat.Model.RealChatViewModel
import com.kbline.kotlin_module.Util.Uuid
import com.kbline.kotlin_module.databinding.ActivityRealChatBinding
import kotlinx.android.synthetic.main.activity_real_chat.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class RealChatActivity : KbActivity<ActivityRealChatBinding,RealChatViewModel>() {
    override val reId: Int
        get() = R.layout.activity_real_chat
    override val viewModel: RealChatViewModel by viewModel()

    var my_uuid : String = ""


    override fun viewStart() {
        my_uuid = Uuid.get(applicationContext)!! //디바이스 고유값.
        viewModel.initFirebase()

        recycler.run {

            layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)

        }


    }

    override fun bindStart() {

        //메시지가 업데이트 되면 실시간으로 어뎁터에 뷰를 뿌려줍니다.
        viewModel.messageData.observe(this, Observer {

            var messageAdapter = MessageAdapter(it,my_uuid)
            recycler.adapter = messageAdapter
            messageAdapter.notifyDataSetChanged()
        })

        //메시지를 확인해줍니다.
        viewModel.getMessage()
    }

    override fun bindAfter() {

        //메시지를 전송합니다.
        sendBtn.setOnClickListener {
            viewModel.sendMessage(my_uuid,messageEdit.text.toString())
            messageEdit.setText("")
        }


        //EditText의 빈값 유무에 따라 전송 버튼을 노출합니다.
        dis.add(RxTextView.textChanges(messageEdit)
            .filter {

                sendBtn.visibility = View.GONE
                it.isNotEmpty()

            }.subscribe {
                sendBtn.visibility = View.VISIBLE

            })

    }

}
