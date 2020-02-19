package com.kbline.kotlin_module.MVVM.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kbline.kotlin_module.MVVM.ApiData.ApiResponse
import com.kbline.kotlin_module.R
import com.kbline.kotlin_module.Util.KbImage

import kotlinx.android.synthetic.main.listitem_search_item.view.*

class TrendAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.listitem_search_item,parent,false)
    ) {
        fun onBind(item : ApiResponse.Item) {

            itemView.run {

                KbImage.image(item.images.fixed_width_small.url,sImageView)
            }

        }
    }

    private val getData = ArrayList<ApiResponse.Item>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemHolder(parent)
    override fun getItemCount() = getData.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ItemHolder)?.onBind(getData[position])
    }

    fun addItem(item : ApiResponse.Item) {
        getData.add(item)
      //  getData.add()
    }

    fun clear() {
        getData.clear()
        notifyDataSetChanged()
    }
}