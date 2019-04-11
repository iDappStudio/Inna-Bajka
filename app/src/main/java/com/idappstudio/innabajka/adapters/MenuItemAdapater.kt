package com.idappstudio.innabajka.adapters

import android.content.Context
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.model.MenuItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class MenuItemAdapater(private val item: MenuItem, private val context: Context) : Item() {

    override fun bind(holder: ViewHolder, position: Int) {


    }

    override fun getLayout(): Int = R.layout.list_item_card_small

}