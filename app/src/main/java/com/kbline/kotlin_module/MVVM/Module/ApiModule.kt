package com.kbline.kotlin_module.MVVM.Module

import com.kbline.kotlin_module.MVVM.Adapter.TrendAdapter
import com.kbline.kotlin_module.MVVM.ApiUtil.ApiService
import com.kbline.kotlin_module.MVVM.ViewModel.GiphyViewModel

import com.kimbongline.giphy.Util.ApiFolder.DataModel
import com.kimbongline.giphy.Util.ApiFolder.DataModelImpl
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

var retrofit = module{
    single<ApiService> {
        Retrofit.Builder()
            .baseUrl("http://api.giphy.com/v1/gifs/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)


    }


}

var trendAdapter = module {
    factory {
        TrendAdapter()
    }
}

var model = module {
    factory<DataModel> {
        DataModelImpl(get())
    }
}

var giphyViewModel = module {
    viewModel {
        GiphyViewModel(get())
    }
}


var apiModule = listOf(
    retrofit,
    trendAdapter,
    model,
    giphyViewModel
)