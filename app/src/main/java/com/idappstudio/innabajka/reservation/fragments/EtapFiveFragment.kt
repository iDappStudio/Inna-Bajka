package com.idappstudio.innabajka.reservation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.reservation.SetReservationActivity.Companion.viewPager

class EtapFiveFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_etap_five, container, false)

        code = rootView.findViewById<EditText>(R.id.codeEdit)
        phone = rootView.findViewById<EditText>(R.id.phoneEdit)
        val next = rootView.findViewById<Button>(R.id.next)

        next.isEnabled = false

        next.setOnClickListener {

            viewPager.currentItem = 5

        }

        code.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(s?.trimStart()?.trimEnd()?.length!! > 1){

                    code.backgroundTintList = ContextCompat.getColorStateList(code.context!!, R.color.colorAccent)

                } else {

                    code.backgroundTintList = ContextCompat.getColorStateList(code.context!!, R.color.colorPrimaryDark)

                }

            }

        })

        phone.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(s?.trimStart()?.trimEnd()?.length!! == 9){

                    next.isEnabled = true
                    next.backgroundTintList = ContextCompat.getColorStateList(phone.context!!, R.color.colorAccent)
                    next.text = "dalej"

                    phone.backgroundTintList = ContextCompat.getColorStateList(phone.context!!, R.color.colorAccent)

                } else {

                    next.isEnabled = false
                    next.backgroundTintList = ContextCompat.getColorStateList(phone.context!!, R.color.colorPrimaryDark)
                    next.text = "wpisz numer telefonu"

                    phone.backgroundTintList = ContextCompat.getColorStateList(phone.context!!, R.color.colorPrimaryDark)

                }

            }

        })

        return rootView
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var code: EditText
        lateinit var phone: EditText

    }

}
