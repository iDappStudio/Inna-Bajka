package com.idappstudio.innabajka.adapters

import com.google.android.gms.maps.model.Marker
import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.maps.GoogleMap
import com.idappstudio.innabajka.R


class CustomInfoWindowAdapter(private val mContext: Context) : GoogleMap.InfoWindowAdapter {

    private val mWindow: View = LayoutInflater.from(mContext).inflate(R.layout.custom_info_nav, null)

    private fun rendowWindowText(marker: Marker, view: View) {

        val layout: ConstraintLayout = view.findViewById(R.id.include)
        val text = layout.findViewById<TextView>(R.id.head_title_text)
        text.text = "inna bajka"
    }

    override fun getInfoWindow(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}