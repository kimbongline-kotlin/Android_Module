package com.kbline.kotlin_module

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.disposables.CompositeDisposable


//MVVM Activity
abstract class KbActivity<T : ViewDataBinding, R : KbViewModel> : AppCompatActivity() {

    lateinit var viewDataBinding: T
    abstract val reId : Int
    abstract val viewModel : R

    abstract fun viewStart()
    abstract fun bindStart()
    abstract fun bindAfter()
    val dis = CompositeDisposable()


    override fun onCreate(paramBundle: Bundle?) {
        super.onCreate(paramBundle)
    //    supportActionBar!!.hide()

        viewDataBinding = DataBindingUtil.setContentView(this,reId)

        viewStart()
        bindStart()
        bindAfter()

    }


    override fun onStop() {
        super.onStop()
        if(isFinishing) {
            dis.clear()
        }
    }



}