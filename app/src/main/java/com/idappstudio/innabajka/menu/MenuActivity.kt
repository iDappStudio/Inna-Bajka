package com.idappstudio.innabajka.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_menu.*
import androidx.viewpager.widget.ViewPager
import com.google.firebase.firestore.FirebaseFirestore
import com.idappstudio.innabajka.NavigationActivity
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.interfaces.Loading
import com.idappstudio.innabajka.menu.fragments.MenuFragment
import android.widget.ProgressBar


class MenuActivity : AppCompatActivity() {

    private val tabIcons = intArrayOf(
        R.drawable.ic_cake_slice,
        R.drawable.ic_soda,
        R.drawable.ic_coffee_cup,
        R.drawable.ic_cocktail,
        R.drawable.ic_wine,
        R.drawable.ic_beer)

    private val db = FirebaseFirestore.getInstance().collection("menu").document("menu")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        loadingVar = loading

        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.ic_round_menu_24px)
        toolbar.setNavigationOnClickListener {

            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()

        }

        supportActionBar?.title = "Menu"

        setupViewPager(viewpager)

        tabs.setupWithViewPager(viewpager)
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(p0: TabLayout.Tab?) {

                supportActionBar?.title = mFragmentTitleList[p0!!.position]
            }

        })

    }

    private fun setupTabIcons(size: Int) {

        viewpager.offscreenPageLimit = size

        for(i in 0..size) {
            tabs.getTabAt(i)?.setIcon(tabIcons[i])
        }

    }

    private fun setupViewPager(viewPager: ViewPager) {

        val adapter = ViewPagerAdapter(supportFragmentManager)

        db.get().addOnSuccessListener {

            if(it != null){

                if(it.exists()){

                    val list: ArrayList<String> = it["category"] as ArrayList<String>

                    list.forEach { it2 ->

                        val args = Bundle()
                        args.putString("category", it2)

                        val fragment = MenuFragment()
                        fragment.arguments = args

                        adapter.addFragment(fragment, it2)

                        if(adapter.count == list.size){

                            viewPager.adapter = adapter
                            setupTabIcons(list.size)

                        }

                    }

                }

            }

        }

    }

    internal inner class ViewPagerAdapter(manager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(manager) {

        private val mFragmentList: ArrayList<androidx.fragment.app.Fragment> = ArrayList()

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

        val mFragmentTitleList: ArrayList<String> = ArrayList()

        lateinit var loadingVar: ProgressBar

        fun goneLoading(){

            loadingVar.visibility = View.GONE

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()

    }

}
