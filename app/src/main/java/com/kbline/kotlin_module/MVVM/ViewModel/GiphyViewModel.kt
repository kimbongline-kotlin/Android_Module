package com.kbline.kotlin_module.MVVM.ViewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kbline.kotlin_module.KbViewModel
import com.kbline.kotlin_module.MVVM.ApiData.ApiResponse
import com.kimbongline.giphy.Util.ApiFolder.DataModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GiphyViewModel(private val model : DataModel) : KbViewModel() {

    private val _trendData = MutableLiveData<ApiResponse>()
    val trendData : LiveData<ApiResponse> get() = _trendData



    fun getData(api : String) {

        add(model.getData(api)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.run {
                    if (data.size > 0) {

                        _trendData.postValue(this)
                    }

                }
            }, {
                Log.d(TAG, "message : ${it.message}")
            }))
    }

    fun search_data(api : String, keyword : String) {

        add(model.getSearchData(api,keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.run {
                    if(data.size > 0) {
                        _trendData.postValue(this)
                    }

                }
            }, {
                Log.d(TAG, "message : ${it.message}")
            }))
    }
}