package com.kbline.kotlin_module.GalleryUtil.Fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

import bsc2086.kotlin_module.R
import com.kbline.kotlin_module.GalleryUtil.Util.CameraValue
import com.kbline.kotlin_module.GalleryUtil.Util.Gallery
import com.kbline.kotlin_module.GalleryUtil.Util.KbGalleryAdapter
import com.kbline.kotlin_module.Util.KbImage
import kotlinx.android.synthetic.main.fragment_gallery.*


class GalleryFragment : Fragment() {

    lateinit public var mAdapter: KbGalleryAdapter
    public var sel_photo: ArrayList<String> = ArrayList()
    public var multiPhoto : ArrayList<String> = ArrayList()

    var type : Int = 0;
    var max_value : Int = 1;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        gallery_RecyclerView.layoutManager = GridLayoutManager(context,4)
        gallery_RecyclerView.setHasFixedSize( true)

        if(activity!!.intent.getIntExtra(CameraValue.MAX_VALUE,0) != null) {
            max_value = activity!!.intent.getIntExtra(CameraValue.MAX_VALUE,1)
        }else {
            max_value = activity!!.intent.getIntExtra(CameraValue.MAX_VALUE,1)
        }

        if(activity!!.intent.getIntExtra(CameraValue.TYPE,0) != null) {
            type = activity!!.intent.getIntExtra(CameraValue.TYPE,0)
            if(type == 0) {
                multiSelectBtn.setText("여러장 선택")
            }else {
                multiSelectBtn.setText("한장 선택")
            }
        }else {
            type = 0

        }

        nextBtn.setOnClickListener {

            if( type == 0) {
                var uri = ArrayList<String>()
                uri.add(single_url)
                var intent = Intent()
                intent.putExtra(CameraValue.IMAGES,uri)
                activity!!.setResult(RESULT_OK,intent)
                activity!!.finish()
            }else {
                var uri = ArrayList<String>()
                for (i in 0..mAdapter.sel_photo.size-1) {
                    uri.add(sel_photo.get(i))
                    Log.d("object",uri.get(i))
                }


                var intent = Intent()
                intent.putExtra(CameraValue.IMAGES,uri)
                activity!!.setResult(RESULT_OK,intent)
                activity!!.finish()
            }

        }


        activity!!.runOnUiThread {

            Log.d("object",type.toString())

            if(type == 0) {
                mAdapter = KbGalleryAdapter(activity as Activity,this,gallery_RecyclerView,sel_photo,1)
                Gallery(activity as Activity,mAdapter).ImageList()
                gallery_RecyclerView.adapter = mAdapter
            }else {
                mAdapter = KbGalleryAdapter(activity as Activity,this,gallery_RecyclerView,sel_photo,max_value)
                Gallery(activity as Activity,mAdapter).ImageList()
                gallery_RecyclerView.adapter = mAdapter
            }

            multiSelectBtn.setOnClickListener {
                if(multiSelectBtn.text.toString().equals("한장")) {
                    type = 0
                    multiSelectBtn.setText("여러장 선택")

                    sel_photo.clear()
                    multiPhoto.clear()

                    mAdapter = KbGalleryAdapter(activity as Activity, this, gallery_RecyclerView, sel_photo, Integer.valueOf(1))
                    Gallery(activity as Activity, mAdapter).ImageList()
                    gallery_RecyclerView!!.setAdapter(mAdapter)


                    mAdapter.notifyDataSetChanged()
                    
                }else {
                    type = 1
                    multiSelectBtn.setText("한장 선택")

                    sel_photo.clear()
                    multiPhoto.clear()
                    
                    mAdapter = KbGalleryAdapter(activity as Activity, this, gallery_RecyclerView, sel_photo, Integer.valueOf(max_value))
                    Gallery(activity as Activity, mAdapter).ImageList()
                    gallery_RecyclerView!!.setAdapter(mAdapter)


                    mAdapter.notifyDataSetChanged()
                    
                }
            }
        }




    }

    var single_url = "";

    fun showImage(url: String) {
        single_url = url;

        KbImage.image(url,cropView)

    }


}
