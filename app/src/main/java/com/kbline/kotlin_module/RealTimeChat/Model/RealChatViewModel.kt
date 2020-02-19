package com.kbline.kotlin_module.RealTimeChat.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.kbline.kotlin_module.KbViewModel

class RealChatViewModel  : KbViewModel() {

    var databaseReference: DatabaseReference? = null
    private val _messageData = MutableLiveData<ArrayList<Message>>()
    val messageData : LiveData<ArrayList<Message>> get() = _messageData


    fun initFirebase() {
        databaseReference = FirebaseDatabase.getInstance().reference
    }

    fun getMessage() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val toReturn: ArrayList<Message> = ArrayList();

                for(data in dataSnapshot.children){
                    val messageData = data.getValue<Message>(
                        Message::class.java)
                    val message = messageData?.let { it } ?: continue
                    toReturn.add(message)
                }
                _messageData.postValue(toReturn)

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        databaseReference!!.addValueEventListener(postListener)
    }

    fun sendMessage(uuid : String, text : String) {
       databaseReference!!.child(System.currentTimeMillis().toString())
            .setValue(Message(uuid,text,System.currentTimeMillis()))
    }
}