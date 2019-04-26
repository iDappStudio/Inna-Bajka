package com.idappstudio.innabajka.tip.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.tip.TipActivity.Companion.tabsLayout
import com.idappstudio.innabajka.tip.data.TipData
import com.idappstudio.innabajka.tip.fragments.EtapOneTipFragment.Companion.name
import com.idappstudio.innabajka.tip.fragments.EtapThreeTipFragment.Companion.comment
import com.idappstudio.innabajka.tip.fragments.EtapTwoTipFragment.Companion.tip_amount

import pl.mobiltek.paymentsmobile.dotpay.Configuration
import pl.mobiltek.paymentsmobile.dotpay.events.PaymentEndedEventArgs
import pl.mobiltek.paymentsmobile.dotpay.events.PaymentManagerCallback
import pl.mobiltek.paymentsmobile.dotpay.managers.PaymentManager
import pl.mobiltek.paymentsmobile.dotpay.model.PaymentInformation
import java.util.HashMap

class EtapFourTipFragment : Fragment(), PaymentManagerCallback {

    override fun onPaymentSuccess(p0: PaymentEndedEventArgs?) {

    }

    override fun onPaymentFailure(p0: PaymentEndedEventArgs?) {

    }

    private lateinit var person: TextView
    private lateinit var tip_amount2: TextView
    private lateinit var comment2: TextView

    private lateinit var next: Button

    private val NAME = "firstname"
    private val LAST_NAME = "lastname"

    private var pay = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_etap_four_tip, container, false)

        next = rootView.findViewById(R.id.next)

        person = rootView.findViewById(R.id.personName)
        tip_amount2 = rootView.findViewById(R.id.tipText)
        comment2 = rootView.findViewById(R.id.commentText)

        next.setOnClickListener {

            PaymentManager.getInstance().initialize(this.activity, getPaymentInformation())

        }

        setupSDK()

        return rootView
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)

        if(menuVisible) {

            check()

            person.text = if (name.text.isNullOrBlank()) {
                "Anonimowy"
            } else {
                name.text.toString().trimStart().trimEnd()
            }
            tip_amount2.text = tip_amount.text.toString() + " zł"
            comment2.text = if (comment.text.isNullOrBlank()) {
                "Brak"
            } else {
                comment.text.toString().trimStart().trimEnd()
            }

            val tabStrip = tabsLayout.getChildAt(0) as LinearLayout

            for (i in 0 until tabStrip.childCount) {
                tabStrip.getChildAt(i).setOnTouchListener { v, event -> false }
            }

        }

    }

    private fun check(){

        if(tip_amount.text.toString().toDoubleOrNull() != null) {

            if (tip_amount.text.toString().toDouble() >= 1.00) {

                pay = true

                next.isEnabled = true
                next.backgroundTintList = ContextCompat.getColorStateList(tip_amount.context!!, R.color.colorAccent)
                next.text = "dalej"

            } else {

                pay = false

                next.isEnabled = false
                next.backgroundTintList = ContextCompat.getColorStateList(tip_amount.context!!, R.color.colorPrimaryDark)
                next.text = "zapomniałeś o kwocie"

            }

        } else {

            pay = false

            next.isEnabled = false
            next.backgroundTintList = ContextCompat.getColorStateList(tip_amount.context!!, R.color.colorPrimaryDark)
            next.text = "zapomniałeś o kwocie"

        }

    }

    @NonNull
    private fun getPaymentInformation(): PaymentInformation {

        val amount = tip_amount.text.toString().toDouble()

        val paymentInformation = PaymentInformation(TipData.merchantId, amount, TipData.description, TipData.currency)

        val str = person.text.toString().split(' ').toMutableList()

        val sender = HashMap<String, String>()
        sender[NAME] = str[0]

        if(str.size > 1){

            sender[LAST_NAME] = str[1]

        }

        paymentInformation.senderInformation = sender
        return paymentInformation
    }

    private fun setupSDK() {

        Configuration.setToolBarBackgroundColor(R.color.colorPrimaryDark)
        Configuration.setToolBarTitleTextColor(R.color.colorWhite)
        Configuration.setPaymentInfoBackgroundColor(R.color.colorPrimaryDark)

        PaymentManager.getInstance().setPaymentManagerCallback(this)
        PaymentManager.getInstance().applicationVersion = Configuration.TEST_VERSION

    }

}
