package com.kbline.kotlin_module.RealTimeChat.Model

import androidx.annotation.Keep

class Message {
    constructor()
    constructor(uuid : String, text : String, time : Long){
        this.text = text
        this.uuid = uuid
        this.time = time
    }
    var uuid : String? = null
    var text: String? = null
    var time: Long = System.currentTimeMillis()
}