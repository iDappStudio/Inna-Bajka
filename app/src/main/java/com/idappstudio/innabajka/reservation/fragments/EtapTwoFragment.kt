package com.idappstudio.innabajka.reservation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.reservation.SetReservationActivity.Companion.viewPager
import java.util.*




class EtapTwoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_etap_two, container, false)

        calendar = rootView.findViewById<CalendarView>(R.id.calendarView)
        val next = rootView.findViewById<Button>(R.id.next)

        next.isEnabled = true
        next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
        next.text = "dalej"

        next.setOnClickListener {

            viewPager.currentItem = 2

        }

        val c = Calendar.getInstance().time
        calendar.minDate = c.time

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->

            date.set(year, month, dayOfMonth)

            if(year >= Calendar.getInstance().get(Calendar.YEAR) &&
                month >= Calendar.getInstance().get(Calendar.MONTH)) {

                if(dayOfMonth == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && month == Calendar.getInstance().get(Calendar.MONTH)) {

                    next.isEnabled = true
                    next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                    next.text = "dalej"

                    dataCheck = true

                } else {

                    next.isEnabled = true
                    next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                    next.text = "dalej"

                    dataCheck = false

                }

            } else {

                next.isEnabled = false
                next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
                next.text = "Wybierz termin"

                dataCheck = false

            }

        }

        return rootView
    }

    companion object {

        var dataCheck: Boolean = true
        var date: Calendar = Calendar.getInstance()

        @SuppressLint("StaticFieldLeak")
        lateinit var calendar: CalendarView

    }

}
