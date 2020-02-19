package com.kbline.kotlin_module.MVVM.ApiData

data class ApiResponse(var data : List<Item>) {

    data class Item(var id : String, var images : Image) {
        data class Image(val downsized_medium : getUrl, val fixed_width_small : getUrl ) {
            data class getUrl(var url : String)
        }
    }
}