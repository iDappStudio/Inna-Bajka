package com.idappstudio.innabajka.tip.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat

import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.tip.TipActivity.Companion.viewPager

class EtapTwoTipFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_etap_two_tip, container, false)

        tip_amount = rootView.findViewById(R.id.nameEdit)
        val next = rootView.findViewById<Button>(R.id.next)

        next.setOnClickListener {

            viewPager.currentItem = 2

        }

        next.isEnabled = false
        next.backgroundTintList = ContextCompat.getColorStateList(tip_amount.context!!, R.color.colorPrimaryDark)
        next.text = "wpisz kwotę"

        tip_amount.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(s?.toString()?.toDoubleOrNull() != null) {

                    if (s.toString().toDouble() >= 1.00) {

                        next.isEnabled = true
                        next.backgroundTintList = ContextCompat.getColorStateList(tip_amount.context!!, R.color.colorAccent)
                        next.text = "dalej"

                        tip_amount.backgroundTintList =
                            ContextCompat.getColorStateList(tip_amount.context!!, R.color.colorAccent)

                    } else {

                        next.isEnabled = false
                        next.backgroundTintList = ContextCompat.getColorStateList(tip_amount.context!!, R.color.colorPrimaryDark)
                        next.text = "wpisz kwotę"

                        tip_amount.backgroundTintList =
                            ContextCompat.getColorStateList(tip_amount.context!!, R.color.colorPrimaryDark)

                    }

                } else {

                    next.isEnabled = false
                    next.backgroundTintList = ContextCompat.getColorStateList(tip_amount.context!!, R.color.colorPrimaryDark)
                    next.text = "wpisz kwotę"

                    tip_amount.backgroundTintList =
                        ContextCompat.getColorStateList(tip_amount.context!!, R.color.colorPrimaryDark)

                }

            }

        })

        return rootView
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var tip_amount: EditText

    }


}
