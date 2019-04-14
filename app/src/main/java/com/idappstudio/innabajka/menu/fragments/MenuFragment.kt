package com.idappstudio.innabajka.menu.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.adapters.HeaderItem
import com.idappstudio.innabajka.adapters.MenuItemAdapater
import com.idappstudio.innabajka.model.MenuItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class MenuFragment(private val category: String) : androidx.fragment.app.Fragment() {

    private lateinit var rv: RecyclerView

    private val db = FirebaseFirestore.getInstance().collection("menu").document("menu").collection(category)

    private val menuAdapter = GroupAdapter<ViewHolder>()

    private val menuSections = HashMap<String, Section>()

    private val menuItems = HashMap<String, ArrayList<MenuItemAdapater>>()

    private fun addItem(item: MenuItem) {
        menuItems[item.podkategoria]?.add(MenuItemAdapater(item, this.context!!))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_menu, container, false)

        rv = rootView.findViewById(R.id.rv)

        initRV {

            rv.adapter = menuAdapter

            getItems {

            }

        }

        return rootView

    }

    private fun initRV(onComplete: () -> Unit){

        db.get().addOnSuccessListener {

            if(it != null){

                if(!it.isEmpty){

                    var i = 0

                    for(doc in it.documents){

                        i++

                        if(!menuSections.containsKey(doc.getString("podkategoria")!!)){

                            menuSections[doc.getString("podkategoria")!!] = Section()
                            menuSections[doc.getString("podkategoria")!!]?.setHeader(HeaderItem(doc.getString("podkategoria")!!))
                            menuSections[doc.getString("podkategoria")!!]?.setHideWhenEmpty(true)
                            menuItems[doc.getString("podkategoria")!!] = ArrayList()
                            val rvLLM = LinearLayoutManager(this.context)
                            rvLLM.orientation = RecyclerView.VERTICAL
                            rv.layoutManager = rvLLM
                            menuAdapter.add(menuSections[doc.getString("podkategoria")!!]!!)

                        }

                        if(i == it.size()) {
                            onComplete()
                        }

                    }

                }

            }

        }

    }

    private fun getItems(onComplete: () -> Unit){

        db.get().addOnSuccessListener {

            if(it != null){

                if(!it.isEmpty){

                    for(doc in it.documents){

                        val item: MenuItem = doc.toObject(MenuItem::class.java)!!
                        addItem(item)
                        menuSections[item.podkategoria]?.update(menuItems[item.podkategoria]!!)

                    }

                }

            }

        }

    }

}
