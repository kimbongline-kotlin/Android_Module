package com.kimbongline.giphy.Util.ApiFolder

import com.kbline.kotlin_module.MVVM.ApiData.ApiResponse
import com.kbline.kotlin_module.MVVM.ApiUtil.ApiService
import io.reactivex.Single

class DataModelImpl(private val service: ApiService):DataModel{


    override fun getData(api: String): Single<ApiResponse> {
        return service.getTrend(api)
    }

    override fun getSearchData(api: String, keyword: String): Single<ApiResponse> {
        return  service.searchKeyword(api,keyword)
    }
}