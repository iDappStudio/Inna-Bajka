package com.idappstudio.innabajka.reservation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.reservation.SetReservationActivity.Companion.viewPager
import com.idappstudio.innabajka.reservation.fragments.EtapTwoFragment.Companion.dataCheck
import java.util.*

class EtapThreeFragment : Fragment() {

    private lateinit var next: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_etap_three, container, false)

        time = rootView.findViewById<TimePicker>(R.id.timeView)
        next = rootView.findViewById<Button>(R.id.next)

        next.isEnabled = true
        next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
        next.text = "dalej"

        next.setOnClickListener {

            viewPager.currentItem = 3

        }

        time.setIs24HourView(true)

        if(time.minute < 30){

            time.minute = time.minute + 30

        } else {

            time.minute - 60
            time.hour = time.hour + 1
            time.minute = (time.minute - 60) + 30

        }

        time.setOnTimeChangedListener { _, hourOfDay, minute ->

            EtapTwoFragment.date.set(EtapTwoFragment.date.get(Calendar.YEAR), EtapTwoFragment.date.get(Calendar.MONTH), EtapTwoFragment.date.get(Calendar.DAY_OF_MONTH), hourOfDay, minute, 0)

            if(!dataCheck){

                next.isEnabled = true
                next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                next.text = "dalej"

            } else {

                if(hourOfDay >= Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {

                    if(minute <= Calendar.getInstance().get(Calendar.MINUTE) && hourOfDay == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {

                        next.isEnabled = false
                        next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
                        next.text = "ustaw godzine"

                    } else {

                        next.isEnabled = true
                        next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                        next.text = "dalej"

                    }

                } else {

                    next.isEnabled = false
                    next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
                    next.text = "ustaw godzine"

                }

            }

        }

        return rootView
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var time: TimePicker

    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)

        if(menuVisible) {

            EtapTwoFragment.date.set(
                EtapTwoFragment.date.get(Calendar.YEAR),
                EtapTwoFragment.date.get(Calendar.MONTH),
                EtapTwoFragment.date.get(Calendar.DAY_OF_MONTH),
                time.hour,
                time.minute,
                0
            )

            if (!dataCheck) {

                next.isEnabled = true
                next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                next.text = "dalej"

            } else {

                if (time.hour >= Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {

                    if (time.minute <= Calendar.getInstance().get(Calendar.MINUTE) && time.hour == Calendar.getInstance().get(
                            Calendar.HOUR_OF_DAY)) {

                        next.isEnabled = false
                        next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
                        next.text = "ustaw godzine"

                    } else {

                        next.isEnabled = true
                        next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                        next.text = "dalej"

                    }

                } else {

                    next.isEnabled = false
                    next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
                    next.text = "ustaw godzine"

                }

            }
        }

    }

}
