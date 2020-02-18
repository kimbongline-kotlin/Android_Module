package com.kbline.kotlin_module.SharePreferenceUtil

import android.content.Context
import android.preference.PreferenceManager
import org.json.JSONArray
import org.json.JSONException
import java.util.*

object ArraySaved  {

    fun saveItem(
        context: Context,
        key: String,
        values: ArrayList<String>
    ) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in values.indices) {
            a.put(values[i])
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString())
        } else {
            editor.putString(key, null)
        }
        editor.apply()
    }

    fun calledItem(
        context: Context,
        key: String
    ): ArrayList<String> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val json = prefs.getString(key, null)
        val urls = ArrayList<String>()
        if (json != null) {
            try {
                val a = JSONArray(json)
                for (i in 0 until a.length()) {
                    val url = a.optString(i)
                    urls.add(url)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return urls
    }


}