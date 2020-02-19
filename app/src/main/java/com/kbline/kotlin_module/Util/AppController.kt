package com.kbline.kotlin_module.Util

import android.app.Application
import com.kbline.kotlin_module.Util.Module.apiModule
import org.koin.android.ext.android.startKoin

class AppController : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(applicationContext,
            apiModule
        )
    }
}