package com.idappstudio.innabajka.adapters

import com.idappstudio.innabajka.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.header_title.*
import kotlinx.android.synthetic.main.header_title.view.*

class HeaderItem(private val title: String) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.head_title_text.text = title

    }

    override fun getLayout() = R.layout.header_title

}