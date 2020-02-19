package com.kbline.kotlin_module.MVVM.ApiUtil

import com.kbline.kotlin_module.MVVM.ApiData.ApiResponse

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("trending")
    fun getTrend(@Query("api_key") key : String) : Single<ApiResponse>

    @GET("search")
    fun searchKeyword(@Query("api_key") key : String, @Query("q") keyword : String) : Single<ApiResponse>
}