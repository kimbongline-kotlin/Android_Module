package com.kimbongline.giphy.Util.ApiFolder

import com.kbline.kotlin_module.MVVM.ApiData.ApiResponse
import io.reactivex.Single


interface DataModel {
    fun getData(api : String) : Single<ApiResponse>
    fun getSearchData(api : String, keyword : String) : Single<ApiResponse>
}