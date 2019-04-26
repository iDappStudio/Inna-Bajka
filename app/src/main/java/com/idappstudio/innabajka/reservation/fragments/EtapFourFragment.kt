package com.idappstudio.innabajka.reservation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.reservation.SetReservationActivity.Companion.viewPager

class EtapFourFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_etap_four, container, false)

        number = rootView.findViewById<NumberPicker>(R.id.numberView)
        val next = rootView.findViewById<Button>(R.id.next)

        next.isEnabled = true
        next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
        next.text = "dalej"

        next.setOnClickListener {

            viewPager.currentItem = 4

        }

        number.minValue = 1
        number.maxValue = 9
        number.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        return rootView
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var number: NumberPicker

    }

}
