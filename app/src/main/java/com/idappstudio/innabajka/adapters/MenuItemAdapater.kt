package com.idappstudio.innabajka.adapters

import android.content.Context
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.model.MenuItem
import com.idappstudio.innabajka.utils.GlideUtil
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.list_item_card_small.*

class MenuItemAdapater(private val item: MenuItem, private val context: Context) : Item() {

    override fun bind(holder: ViewHolder, position: Int) {

        holder.tytul.text = item.nazwa

        holder.podtytul.text = "${item.cena} zł • ${item.ilosc} ${item.miara}"

        GlideUtil.setImage(holder.obrazek2, context, item.obrazek)

    }

    override fun getLayout(): Int = R.layout.list_item_card_small

}