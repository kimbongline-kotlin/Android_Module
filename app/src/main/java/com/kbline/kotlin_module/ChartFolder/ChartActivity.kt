package com.kbline.kotlin_module.ChartFolder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kbline.kotlin_module.R

class ChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
    }
}

/*
package com.sochic.sochic.SellerFolder.Fragment


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.binaryfork.spanny.Spanny
import com.crashlytics.android.Crashlytics
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.tabs.TabLayout
import com.sochic.sochic.SellerFolder.*
import com.sochic.sochic.SellerFolder.API.SellerStateInfoAPI
import com.sochic.sochic.SellerFolder.API.SellerStatisticAPI
import com.sochic.sochic.SettingFolder.SettingActivity
import com.sochic.sochic.Util.PriceUtil
import com.sochic.sochic.Util.ScActivity
import io.fabric.sdk.android.Fabric
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.fragment_seller_my_page.*


/**
 * A simple [Fragment] subclass.
 */
class SellerMyPageFragment : Fragment() {

    lateinit var mCon : Context
    lateinit var mAct : Activity
    lateinit var kAct : ScActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mCon = context!!
        mAct = activity!!
        kAct = (mAct as ScActivity)
        // Inflate the layout for this fragment
        return inflater.inflate(com.sochic.sochic.R.layout.fragment_seller_my_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Fabric.with(context, Crashlytics())
        addBtn.setOnClickListener {
            startActivity(Intent(context!!,
                SellerProductUploadActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        chart.setBackgroundColor(Color.WHITE)
        chart.setTouchEnabled(false)

        chart.setDrawGridBackground(false)
        chart.description.isEnabled = false

       // chart.xAxis.valueFormatter = IndexAxisValueFormatter()

        val xAxis: XAxis
        run {
            // // X-Axis Style // //
            xAxis = chart.xAxis
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)

            xAxis.position = XAxis.XAxisPosition.BOTTOM



        }

        val yAxis: YAxis
        run {
            // // Y-Axis Style // //
            yAxis = chart.axisLeft
            chart.axisRight.isEnabled = false

            yAxis.spaceBottom = 150.0f
            yAxis.spaceTop = 150.0f
            yAxis.granularity = 25.0f
            yAxis.axisMaximum = 100f
            yAxis.axisMinimum = 0f
        }

        kotlin.run {

        }

        tabLayer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {

                setData(tab!!.text.toString(),tab!!.position)

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })
        tabLayer.addTab(tabLayer.newTab().setText("방문수"),true)
        tabLayer.addTab(tabLayer.newTab().setText("매출수"))
        tabLayer.addTab(tabLayer.newTab().setText("팔로우"))

      // setData()


        var l = chart.legend
        l.form = Legend.LegendForm.LINE

        calcurateBtn.setOnClickListener {

        }

        reviewBtn.setOnClickListener {

            startActivity(Intent(mAct,SellerContactReviewActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        settingBtn.setOnClickListener {
            startActivity(Intent(mAct,SettingActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        orderBtn.setOnClickListener {
            startActivity(Intent(mAct,SellerOrderActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        changeBtn.setOnClickListener {
            startActivity(Intent(mAct,SellerProductChangeActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        numberBtn.setOnClickListener {
            startActivity(Intent(mAct,SellerNumberActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        calcurateBtn.setOnClickListener {
            startActivity(Intent(mAct,SellerCalcurateActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        getItem()
    }

    override fun onResume() {
        super.onResume()
        getItem()

    }

    fun getItem() {
        kAct.disposable.add(kAct.apiService.SELLER_STATE_INFO_API(kAct.id_user)
            .subscribeOn(kAct.io)
            .observeOn(kAct.thread)
            .subscribeWith(object : DisposableSingleObserver<SellerStateInfoAPI> () {
                override fun onError(e: Throwable?) {
                    kAct.ConnectionError()
                }

                override fun onSuccess(t: SellerStateInfoAPI) {
                    if(t.success)
                    {
                        preCntLabel.setText(PriceUtil.set(t.pre_cnt.toString()))
                        paidCntLabel.setText(PriceUtil.set(t.paid_cnt.toString()))
                        deliveryPreCntLabel.setText(PriceUtil.set(t.delivery_pre_cnt.toString()))
                        deliveryIngCntLabel.setText(PriceUtil.set(t.delivery_ing_cnt.toString()))
                        deliveryComCntLabel.setText(PriceUtil.set(t.delivery_com_cnt.toString()))

                        orderBtn.setText(Spanny("주문확인 ").append(PriceUtil.set(t.type_one_cnt.toString()),StyleSpan(Typeface.BOLD)))
                        changeBtn.setText(Spanny("반품교환 ").append(PriceUtil.set(t.type_two_cnt.toString()),StyleSpan(Typeface.BOLD)))
                        numberBtn.setText(Spanny("송장번호 입력 ").append(PriceUtil.set(t.type_three_cnt.toString()),StyleSpan(Typeface.BOLD)))
                    }
                }
            }))
    }

    private fun setData(title : String, position : Int) {


        kAct.disposable.add(kAct.apiService.SELLER_STATISTIC_API(kAct.id_user,position)
            .subscribeOn(kAct.io)
            .observeOn(kAct.thread)
            .subscribeWith(object : DisposableSingleObserver<SellerStatisticAPI> () {
                override fun onError(e: Throwable?) {
                    kAct.ConnectionError()
                }

                override fun onSuccess(t: SellerStatisticAPI) {
                    if(t.success) {
                        var dateItem = ArrayList<String>()
                        val values = ArrayList<Entry>()
                        for (i in 0 ..t.date_response.size) {
                            if(i == t.date_response.size) {
                                chart.xAxis.valueFormatter = IndexAxisValueFormatter(dateItem)
                                val set1: LineDataSet

                                set1 = LineDataSet(values, title)

                                set1.axisDependency = YAxis.AxisDependency.LEFT
                                set1.setColor(Color.BLACK);
                                set1.setLineWidth(1.0f);
                                set1.setDrawValues(false);
                                set1.setDrawCircles(false);
                                set1.setMode(LineDataSet.Mode.LINEAR);
                                set1.setDrawFilled(false);

                                val dataSets = ArrayList<ILineDataSet>()
                                dataSets.add(set1) // add the data sets

                                val data = LineData(dataSets)
                                set1.notifyDataSetChanged()

                                chart.data = data
                                chart.invalidate()
                            }else {
                                values.add(Entry(i.toFloat(), t.cnt_response.get(i).cnt.toFloat()))
                                dateItem.add(t.date_response.get(i).date)
                            }
                        }
                    }
                }
            }))







    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden) {
            getItem()
        }
    }

}

 */