package com.kbline.kotlin_module.GalleryUtil.Util

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import bsc2086.kotlin_module.R
import com.bumptech.glide.Glide
import com.kbline.kotlin_module.GalleryUtil.Fragment.GalleryFragment
import com.kbline.kotlin_module.GalleryUtil.Util.KbGalleryAdapter.ListItemViewHolder
import java.util.*

class KbGalleryAdapter(
    private val mContext: Activity,
    private val mFragment: GalleryFragment,
    private val mRecyclerView: RecyclerView,
    sel_photo: ArrayList<String>,
    max: Int
) : RecyclerView.Adapter<ListItemViewHolder>() {

    private var mListData: ArrayList<GalleryRecyclerView_DTO> =
        ArrayList<GalleryRecyclerView_DTO>()
    private var maxSize = Integer.valueOf(1)
    var sel_photo: ArrayList<String> = ArrayList<String>()

    class ListItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val recyclerView_item_ImageView: ImageView
        val recyclerView_item_Layout: View

        init {
            recyclerView_item_ImageView =
                itemView.findViewById<View>(R.id.recyclerView_item_ImageView) as ImageView
            recyclerView_item_Layout =
                itemView.findViewById(R.id.recyclerView_item_Layout)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_gallery_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.recyclerView_item_Layout.tag = Integer.valueOf(position)
        val mData =
            mListData[position]
        Glide.with(mContext).load(mData!!.strGalleryData)
            .into(holder.recyclerView_item_ImageView)
        if (sel_photo != null) {
            if (sel_photo!!.size == 0) {
                if (position == 0) {
                    this.mFragment.showImage(mData.strGalleryData);
                    holder.recyclerView_item_Layout.setBackgroundColor(
                        Color.parseColor(
                            "#00000000"
                        )
                    )

                } else if (sel_photo!!.contains(mData.strGalleryData)) {
                    holder.recyclerView_item_Layout.setBackgroundColor(
                        Color.parseColor(
                            "#30000000"
                        )
                    )

                } else {
                    holder.recyclerView_item_Layout.setBackgroundColor(
                        Color.parseColor(
                            "#00000000"
                        )
                    )

                }
            } else if (sel_photo!!.contains(mData.strGalleryData)) {
                holder.recyclerView_item_Layout.setBackgroundColor(
                    Color.parseColor(
                        "#30000000"
                    )
                )

            } else {
                holder.recyclerView_item_Layout.setBackgroundColor(
                    Color.parseColor(
                        "#00000000"
                    )
                )

            }
        }
        holder.recyclerView_item_Layout.setOnClickListener { v ->
            val pos = (v.tag as Int).toInt()
            val dto: GalleryRecyclerView_DTO?
            if (maxSize != 1) {
                dto = mListData[pos]
                if (sel_photo != null) {
                    val i = 0
                    if (sel_photo!!.contains(dto!!.strGalleryData)) { // mFragment.removePosition(position);
                        sel_photo!!.removeAt(realPath(dto.strGalleryData) - 1)
                    } else if (sel_photo!!.size < maxSize) {
                        if (sel_photo!!.size == 0) {
                            holder.recyclerView_item_Layout.setBackgroundColor(
                                Color.parseColor(
                                    "#30000000"
                                )
                            )
                            sel_photo!!.add(dto.strGalleryData)
                            //   mFragment.showMultiFirst(dto.strGalleryData,position);
                        } else {
                            holder.recyclerView_item_Layout.setBackgroundColor(
                                Color.parseColor(
                                    "#30000000"
                                )
                            )
                            sel_photo!!.add(dto.strGalleryData)
                            //  mFragment.showMultiImage(dto.strGalleryData,position);
                        }
                    } else {
                        Toast.makeText(mContext, "사진은 최대 ${maxSize}장까지 선택가능합니다.", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    holder.recyclerView_item_Layout.setBackgroundColor(
                        Color.parseColor(
                            "#30000000"
                        )
                    )
                    sel_photo = ArrayList<String>()
                    sel_photo.add(dto!!.strGalleryData)
                }
                notifyDataSetChanged()
            } else {
                dto = mListData[pos]
                if (sel_photo != null) {
                    if (sel_photo!!.size == 1) {
                        holder.recyclerView_item_Layout.setBackgroundColor(
                            Color.parseColor(
                                "#30000000"
                            )
                        )
                        sel_photo!!.clear()
                        sel_photo!!.add(dto!!.strGalleryData)
                        mFragment.showImage(dto.strGalleryData);
                    } else {
                        holder.recyclerView_item_Layout.setBackgroundColor(
                            Color.parseColor(
                                "#30000000"
                            )
                        )
                        sel_photo!!.add(dto!!.strGalleryData)
                         mFragment.showImage(dto.strGalleryData);
                    }
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    fun onCreateData(id: String, data: String, thumb: String?) {
        val grvDTO = GalleryRecyclerView_DTO()
        grvDTO.strGalleryId = id!!
        grvDTO.strGalleryData = data!!
//        grvDTO.strGalleryThumb = thumb!!
        mListData.add(grvDTO)
    }

    fun addData(id: String, data: String, thumb: String?) {
        val grvDTO = GalleryRecyclerView_DTO()
        grvDTO.strGalleryId = id!!
        grvDTO.strGalleryData = data!!
        grvDTO.strGalleryThumb = thumb!!
        mListData.add(1, grvDTO)
    }

    fun realPath(path: String): Int {
        for (i in sel_photo!!.indices) {
            if (sel_photo!![i].toString() == path) {
                return i + 1
            }
        }
        return 0
    }

    init {
        this.sel_photo = sel_photo
        maxSize = max
    }
}