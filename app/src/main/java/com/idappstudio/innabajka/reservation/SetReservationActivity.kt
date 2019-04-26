package com.idappstudio.innabajka.reservation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.reservation.fragments.*
import kotlinx.android.synthetic.main.activity_set_reservation.*
import android.widget.LinearLayout
import com.google.android.material.tabs.TabLayout


class SetReservationActivity : AppCompatActivity() {

    private val tabIcons = intArrayOf(
        R.drawable.ic_person_black_24dp,
        R.drawable.ic_date_range_black_24dp,
        R.drawable.ic_watch_later_black_24dp,
        R.drawable.ic_supervisor_account_black_24dp,
        R.drawable.ic_phone_black_24dp,
        R.drawable.ic_done_black_24dp)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_reservation)

        viewPager = findViewById(R.id.viewpager)
        tabsLayout = findViewById(R.id.tabs)

        setupViewPager()

//        close.setOnClickListener {
//            finish()
//        }

    }


    private fun setupViewPager(){

        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(EtapOneFragment(), "person")
        adapter.addFragment(EtapTwoFragment(), "date")
        adapter.addFragment(EtapThreeFragment(), "time")
        adapter.addFragment(EtapFourFragment(), "people")
        adapter.addFragment(EtapFiveFragment(), "phone")
        adapter.addFragment(EtapSixFragment(), "verfication")

        viewpager.offscreenPageLimit = 5
        viewpager.adapter = adapter

        tabs.setupWithViewPager(viewpager)

        tabs.getTabAt(0)?.setIcon(tabIcons[0])
        tabs.getTabAt(1)?.setIcon(tabIcons[1])
        tabs.getTabAt(2)?.setIcon(tabIcons[2])
        tabs.getTabAt(3)?.setIcon(tabIcons[3])
        tabs.getTabAt(4)?.setIcon(tabIcons[4])
        tabs.getTabAt(5)?.setIcon(tabIcons[5])

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
