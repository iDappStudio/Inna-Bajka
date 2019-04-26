package com.idappstudio.innabajka.tip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.tip.fragments.EtapFourTipFragment
import com.idappstudio.innabajka.tip.fragments.EtapOneTipFragment
import com.idappstudio.innabajka.tip.fragments.EtapThreeTipFragment
import com.idappstudio.innabajka.tip.fragments.EtapTwoTipFragment
import kotlinx.android.synthetic.main.activity_tip.*

class TipActivity : AppCompatActivity() {

    private val tabIcons = intArrayOf(
        R.drawable.ic_person_black_24dp,
        R.drawable.ic_attach_money_black_24dp,
        R.drawable.ic_comment_black_24dp,
        R.drawable.ic_done_black_24dp)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip)

        viewPager = findViewById(R.id.viewpager)
        tabsLayout = findViewById(R.id.tabs)

        setupViewPager()

//        close.setOnClickListener {
//            finish()
//        }

    }


    private fun setupViewPager(){

        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(EtapOneTipFragment(), "person")
        adapter.addFragment(EtapTwoTipFragment(), "tip_amount")
        adapter.addFragment(EtapThreeTipFragment(), "comment")
        adapter.addFragment(EtapFourTipFragment(), "finish")

        viewpager.offscreenPageLimit = 5
        viewpager.adapter = adapter

        tabs.setupWithViewPager(viewpager)

        tabs.getTabAt(0)?.setIcon(tabIcons[0])
        tabs.getTabAt(1)?.setIcon(tabIcons[1])
        tabs.getTabAt(2)?.setIcon(tabIcons[2])
        tabs.getTabAt(3)?.setIcon(tabIcons[3])

        disableTouch()

    }

    private fun disableTouch(){

        viewpager.setOnTouchListener { _, _ -> true }

        val tabStrip = tabs.getChildAt(0) as LinearLayout
        for (i in 0 until tabStrip.childCount) {
            tabStrip.getChildAt(i).setOnTouchListener { v, event -> true }
        }


    }

    internal inner class ViewPagerAdapter(manager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(manager) {

        private val mFragmentList: ArrayList<androidx.fragment.app.Fragment> = ArrayList()
        private val mFragmentTitleList: ArrayList<String> = ArrayList()

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            return mFragmentList[position]
        }

        fun addFragment(fragment: androidx.fragment.app.Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return ""
        }

    }

    companion object {

        lateinit var viewPager: ViewPager
        lateinit var tabsLayout: TabLayout

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
