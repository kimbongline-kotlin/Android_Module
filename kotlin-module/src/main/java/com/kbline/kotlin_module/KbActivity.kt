package com.kbline.kotlin_module

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class KbActivity<T : ViewDataBinding, R : KbViewModel> : AppCompatActivity() {

    lateinit var viewDataBinding: T
    abstract val reId : Int
    abstract val viewModel : R

    abstract fun viewStart()
    abstract fun bindStart()
    abstract fun bindAfter()



    override fun onCreate(paramBundle: Bundle?) {
        super.onCreate(paramBundle)
        supportActionBar!!.hide()

        viewDataBinding = DataBindingUtil.setContentView(this,reId)

        viewStart()
        bindStart()
        bindAfter()

    }

    open fun getRealPathFromURI(
        context: Context,
        contentUri: Uri
    ): String {
        val cursor = context.contentResolver
            .query(contentUri!!, arrayOf("_data"), null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow("_data")
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }


}