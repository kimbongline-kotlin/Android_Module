package com.kbline.kotlin_module.GalleryUtil.Util

import android.app.Activity
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log

class Gallery(private val mActivity: Activity, private val mAdapter: KbGalleryAdapter) {

    private  inner class DoFindImageList() :
        AsyncTask<String?, Int?, Long>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: Long) {
            mAdapter.notifyDataSetChanged()
        }

        override fun onCancelled() {
            super.onCancelled()
        }

        override fun doInBackground(vararg params: String?): Long {
            return findThumbList()
        }
    }

    fun ImageList() {
        DoFindImageList().execute(*arrayOfNulls<String>(0))
    }

    private fun findThumbList(): Long {
        var returnValue: Long = 0
        val grvDTO = GalleryRecyclerView_DTO()
        val imageCursor = mActivity.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf("_id", "_data"),
            null,
            null,
            "date_added desc "
        )
        if (imageCursor != null && imageCursor.count > 0) {
            val imageIDCol = imageCursor.getColumnIndex("_id")
            val imageDataCol = imageCursor.getColumnIndex("_data")
            while (imageCursor.moveToNext()) { //
                 this.mAdapter.onCreateData(imageCursor.getString(imageIDCol), imageCursor.getString(imageDataCol), null);
                returnValue++
            }
        }
        imageCursor!!.close()
        return returnValue
    }

}