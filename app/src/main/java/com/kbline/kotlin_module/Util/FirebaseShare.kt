package com.kbline.kotlin_module.Util

import android.net.Uri
import android.util.Log
import androidx.annotation.NonNull
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.kbline.kotlin_module.BuildConfig
import org.jetbrains.annotations.NotNull
import javax.security.auth.callback.Callback

object FirebaseShare  {


    fun getDynaicUrl(targetUrl : String, shortUrl : String, title : String, info : String, item : String,image_url : String, callback: ShareReturn) {
        var uri = Uri.parse(targetUrl)
        uri = uri.buildUpon().appendQueryParameter("item",item).build()

        var longLinkTask = FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .setLink(uri)
            .setDynamicLinkDomain(shortUrl)
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder(BuildConfig.APPLICATION_ID).setMinimumVersion(1).build())
        //    .setIosParameters(DynamicLink.IosParameters.Builder(BuildConfig.APPLICATION_ID).setAppStoreId("").setMinimumVersion("1").build())
            .setSocialMetaTagParameters(DynamicLink.SocialMetaTagParameters.Builder()
                .setTitle(title)
                .setDescription(info)
                .setImageUrl(Uri.parse(image_url)).build())

        var longUri = longLinkTask.buildDynamicLink().uri

        FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .setLink(uri)
            .setDynamicLinkDomain(shortUrl)
            .setLongLink(longUri)
            .buildShortDynamicLink()
            .addOnCompleteListener {

                Log.d("object",it.toString())
                if(it.isSuccessful) {

                    callback.onSuccess(it.result!!.shortLink.toString())

                }else {

                    callback.onError(it.exception!!.fillInStackTrace())

                }
            }

    }

}

public interface ShareReturn {
    fun onError(@NonNull t : Throwable);
    fun onSuccess(@NonNull url : String)
}