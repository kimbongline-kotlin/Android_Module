package com.kbline.kotlin_module.RealTimeChat.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.kbline.kotlin_module.DateUtil.DateUtil
import com.kbline.kotlin_module.R
import com.kbline.kotlin_module.RealTimeChat.Model.Message
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.message_item.view.*
import java.util.*

class MessageAdapter(

    val messages: ArrayList<Message>,
    val my_uuid : String

) : Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindViewHolder(messages[position])
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
    ) {

        fun onBindViewHolder(message: Message) {
            with(itemView) {


                //메시지의 고유 아이디와 나의 아이디를 비교 하여 뷰를 배치합니다.
                if(message.uuid.toString().equals(my_uuid)) {
                    mDateLabel.visibility = View.VISIBLE
                    uDateLabel.visibility = View.GONE
                    myText.visibility = View.VISIBLE
                    userText.visibility = View.GONE
                    myText.setText(message.text)
                    mDateLabel.setText(DateUtil.convertDate(message.time))
                }else{
                    mDateLabel.visibility = View.GONE
                    uDateLabel.visibility = View.VISIBLE
                    userText.visibility = View.VISIBLE
                    myText.visibility = View.GONE
                    userText.setText(message.text)
                    uDateLabel.setText(DateUtil.convertDate(message.time))
                }


                //채팅창 상단의 오늘/어제/MM월DD일 형식으로 채팅의 시작일을 알려줍니다.

                if(messages.size > 1) {
                    if(position > 0) {

                        var currentData = Date(System.currentTimeMillis())
                        var nowDate = Date(message.time)
                        var beforeDate = Date(messages.get(position-1).time)
                        if(DateUtil.compareToDay(nowDate,beforeDate) == 0) {
                            dateline.visibility = View.GONE
                        }else{
                            dateline.visibility = View.VISIBLE
                            if(DateUtil.compareToDay(currentData,nowDate) == 0) {
                                dateLabel.setText("오늘")
                            }else if(DateUtil.compareToDay(currentData,nowDate) == 1) {
                                dateLabel.setText("어제")
                            }else {
                                dateLabel.setText(DateUtil.convertDateMonth(message.time))
                            }
                        }

                    }else {
                        var currentData = Date(System.currentTimeMillis())
                        var nowDate = Date(message.time)

                        if(DateUtil.compareToDay(currentData,nowDate) == 0) {
                            dateline.visibility = View.VISIBLE
                            dateLabel.setText("오늘")
                        }else if(DateUtil.compareToDay(currentData,nowDate) == 1) {
                            dateline.visibility = View.VISIBLE
                            dateLabel.setText("어제")
                        }else {
                            dateline.visibility = View.VISIBLE
                            dateLabel.setText(DateUtil.convertDateMonth(message.time))
                        }
                    }
                }else {
                    var currentData = Date(System.currentTimeMillis())
                    var nowDate = Date(message.time)

                    if(DateUtil.compareToDay(currentData,nowDate) == 0) {
                        dateline.visibility = View.VISIBLE
                        dateLabel.setText("오늘")
                    }else if(DateUtil.compareToDay(currentData,nowDate) == 1) {
                        dateline.visibility = View.VISIBLE
                        dateLabel.setText("어제")
                    }else {
                        dateline.visibility = View.VISIBLE
                        dateLabel.setText(DateUtil.convertDateMonth(message.time))
                    }
                }




            }
        }
    }



}
