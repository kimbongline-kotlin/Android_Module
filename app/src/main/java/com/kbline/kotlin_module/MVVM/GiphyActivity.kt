package com.kbline.kotlin_module.MVVM

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.arasthel.spannedgridlayoutmanager.SpanSize
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.kbline.kotlin_module.DisplayUtil.Convert
import com.kbline.kotlin_module.KbActivity
import com.kbline.kotlin_module.MVVM.Adapter.TrendAdapter
import com.kbline.kotlin_module.MVVM.ViewModel.GiphyViewModel
import com.kbline.kotlin_module.R
import com.kbline.kotlin_module.RecyclerUtil.SpacesItemDecoration
import com.kbline.kotlin_module.databinding.ActivityGiphyBinding
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_giphy.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class GiphyActivity : KbActivity<ActivityGiphyBinding,GiphyViewModel>() {



    override val reId: Int
        get() = R.layout.activity_giphy
    override val viewModel: GiphyViewModel by viewModel()
    val trendAdapter : TrendAdapter by inject()


    override fun viewStart() {
        Log.d("object","hihi")
        recycler.run {
            adapter = trendAdapter
            var manager = SpannedGridLayoutManager(SpannedGridLayoutManager.Orientation.VERTICAL, 3)
            manager.spanSizeLookup = SpannedGridLayoutManager.SpanSizeLookup { position ->
                if (position == 0) {
                    SpanSize(2, 2)
                } else {
                    SpanSize(1, 1)
                }
            }
            layoutManager = manager
            addItemDecoration(SpacesItemDecoration(Convert.toPixcel(5f,context).toInt(),3))
            setHasFixedSize(true)
        }
    }

    override fun bindStart() {
        viewModel.trendData.observe(this, Observer {

            it.data.forEach {
                trendAdapter.addItem(it)
            }
            trendAdapter.notifyDataSetChanged()
        })
    }



    override fun bindAfter() {
        var api_key = resources.getString(R.string.api_key)
        viewModel.getData(api_key)

        dis.add(RxTextView.textChanges(keywordEdit)
            .filter {
                trendAdapter.clear()
                trendAdapter.notifyDataSetChanged()
                viewModel.getData(api_key)

                it.isNotEmpty()

            }.subscribe {
                trendAdapter.clear()
                trendAdapter.notifyDataSetChanged()
                viewModel.search_data(api_key,it.toString())
            }
        )

    }


}
