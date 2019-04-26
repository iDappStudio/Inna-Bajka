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

class EtapThreeTipFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_etap_three_tip, container, false)

        comment = rootView.findViewById(R.id.commentsEdit)
        val next = rootView.findViewById<Button>(R.id.next)

        next.setOnClickListener {

            viewPager.currentItem = 3

        }

        comment.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(s?.trimStart()?.trimEnd()?.length!! > 0){

                    comment.backgroundTintList = ContextCompat.getColorStateList(comment.context!!, R.color.colorAccent)

                } else {

                    comment.backgroundTintList = ContextCompat.getColorStateList(comment.context!!, R.color.colorPrimaryDark)

                }

            }

        })

        return rootView
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var comment: EditText

    }

}
