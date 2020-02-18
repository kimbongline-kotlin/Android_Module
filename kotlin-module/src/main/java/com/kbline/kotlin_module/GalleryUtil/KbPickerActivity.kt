package com.kbline.kotlin_module.GalleryUtil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import bsc2086.kotlin_module.R
import com.kbline.kotlin_module.GalleryUtil.Adapter.ViewPagerAdapter
import com.kbline.kotlin_module.GalleryUtil.Fragment.CameraFragment
import com.kbline.kotlin_module.GalleryUtil.Fragment.GalleryFragment
import com.kbline.kotlin_module.Util.ColorHex
import kotlinx.android.synthetic.main.activity_kb_picker.*
import java.util.ArrayList

class KbPickerActivity : AppCompatActivity() {

    private val listFragment: ArrayList<Fragment>
        get() {
            val fragments = ArrayList<Fragment>()
            fragments.add(GalleryFragment())
            fragments.add(CameraFragment())
            return fragments
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kb_picker)

        var pagerAdapter = ViewPagerAdapter(supportFragmentManager,listFragment)
        cameraPager.adapter = pagerAdapter
        cameraPager.offscreenPageLimit = 0
        cameraPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                resetBottomBtn()
                when (position) {
                    0 -> libraryBtn.setTextColor(ColorHex.set("#000000"))
                    1 -> cameraBtn.setTextColor(ColorHex.set("#000000"))
                }


            }

            override fun onPageSelected(position: Int) {

            }
        })

        libraryBtn.setOnClickListener {
            cameraPager.setCurrentItem(0)
        }

        cameraBtn.setOnClickListener {
            cameraPager.setCurrentItem(1)
        }
    }

    fun resetBottomBtn() {
        libraryBtn.setTextColor(ColorHex.set("#999999"))
        cameraBtn.setTextColor(ColorHex.set("#999999"))
    }
}
