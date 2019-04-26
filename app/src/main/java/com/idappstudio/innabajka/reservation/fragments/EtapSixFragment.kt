package com.idappstudio.innabajka.reservation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alimuzaffar.lib.pin.PinEntryEditText
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.enums.ReservationStatus
import com.idappstudio.innabajka.model.ReservationItem
import com.idappstudio.innabajka.reservation.SetReservationActivity.Companion.tabsLayout
import com.idappstudio.innabajka.utils.NetworkUtils
import douglasspgyn.com.github.circularcountdown.CircularCountdown
import douglasspgyn.com.github.circularcountdown.listener.CircularListener
import kotlinx.android.synthetic.main.fragment_etap_six.*
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class EtapSixFragment : Fragment() {

    private lateinit var person: TextView
    private lateinit var time: TextView
    private lateinit var date: TextView
    private lateinit var peoples: TextView
    private lateinit var phone: TextView
    private lateinit var error: TextView

    private lateinit var pin: PinEntryEditText
    private lateinit var nextV: Button
    private lateinit var backV: Button
    private lateinit var countdown: CircularCountdown

    private lateinit var verification: ConstraintLayout

    private val auth = FirebaseAuth.getInstance()

    private lateinit var verificationid: String
    private lateinit var code: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_etap_six, container, false)

        val comments = rootView.findViewById<EditText>(R.id.commentsEdit)
        val next = rootView.findViewById<Button>(R.id.next)

        verification = rootView.findViewById(R.id.verifivation)

        pin = verification.findViewById(R.id.pinEdit)
        nextV = verification.findViewById(R.id.finish)
        backV = verification.findViewById(R.id.finish2)
        countdown = verification.findViewById(R.id.circularCountdown)
        error = verification.findViewById(R.id.errroMsg)

        person = rootView.findViewById(R.id.personName)
        time = rootView.findViewById(R.id.timeText)
        date = rootView.findViewById(R.id.dataText)
        peoples = rootView.findViewById(R.id.personNumberText)
        phone = rootView.findViewById(R.id.phoneText)

        next.isEnabled = false

        pin.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(s?.trimStart()?.trimEnd()?.length!! == 6){

                    pin.setPinLineColors(ContextCompat.getColorStateList(pin.context!!, R.color.colorAccent))

                    nextV.isEnabled = true
                    nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                    nextV.text = "Zweryfikuj kod"

                } else {

                    pin.setPinLineColors(ContextCompat.getColorStateList(pin.context!!, R.color.colorPrimaryDark))

                    nextV.isEnabled = false
                    nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
                    nextV.text = "wpisz kod"

                }

            }

        })

        next.setOnClickListener {

            val tabStrip = tabsLayout.getChildAt(0) as LinearLayout
            for (i in 0 until tabStrip.childCount) {
                tabStrip.getChildAt(i).setOnTouchListener { v, event -> true }
            }

            next.visibility = View.INVISIBLE
            comments.visibility = View.INVISIBLE

            verification.visibility = View.VISIBLE

            pin.text = null

            nextV.isEnabled = false
            nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
            nextV.text = "Wpisz kod"

            verification()

            backV.setOnClickListener {

                countdown.stop()
                verification.visibility = View.GONE

                next.visibility = View.VISIBLE
                comments.visibility = View.VISIBLE

                val tabStrip = tabsLayout.getChildAt(0) as LinearLayout
                for (i in 0 until tabStrip.childCount) {
                    tabStrip.getChildAt(i).setOnTouchListener { v, event -> false }
                }

            }

            countdown.create(0, 60, CircularCountdown.TYPE_SECOND).listener(object : CircularListener {

                    override fun onTick(progress: Int) {

                    }

                    override fun onFinish(newCycle: Boolean, cycleCount: Int) {

                        countdown.stop()

                        backV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                        backV.text = "Wyślij ponownie kod"

                        backV.setOnClickListener {

                            countdown.stop()

                            pin.text = null
                            verification()

                            backV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDarker)
                            backV.text = "Zmień Dane"

                            backV.setOnClickListener {

                                backV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDarker)
                                backV.text = "Zmień Dane"

                                countdown.stop()
                                verification.visibility = View.GONE

                                next.visibility = View.VISIBLE
                                comments.visibility = View.VISIBLE

                                val tabStrip = tabsLayout.getChildAt(0) as LinearLayout
                                for (i in 0 until tabStrip.childCount) {
                                    tabStrip.getChildAt(i).setOnTouchListener { v, event -> false }
                                }

                            }

                        }

                    }

                })

        }

        comments.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(s?.trimStart()?.trimEnd()?.length!! > 5){

                    comments.backgroundTintList = ContextCompat.getColorStateList(comments.context!!, R.color.colorAccent)

                } else {

                    comments.backgroundTintList = ContextCompat.getColorStateList(comments.context!!, R.color.colorPrimaryDark)

                }

            }

        })

        return rootView
    }

    private fun check() : Boolean{

        if(person.text.length < 4){
            return false
        }

        if (EtapTwoFragment.dataCheck) {

            if (EtapThreeFragment.time.hour <= Calendar.getInstance().get(Calendar.HOUR_OF_DAY) &&
                EtapThreeFragment.time.minute <= Calendar.getInstance().get(Calendar.MINUTE)) {
                return false
            }

        }

        if(time.text.length != 7){
            return false
        }

        if(date.text.length != 10){
            return false
        }

        if(phone.text.length < 13){
            return false
        }

        return true

    }

    private fun update(){

        person.text = EtapOneFragment.name.text
        date.text = DateFormat.getDateInstance().format(EtapTwoFragment.calendar.date)

        val timeS: String
        val timeH: String

        if(EtapThreeFragment.time.minute < 10) {

            timeS = "0${EtapThreeFragment.time.minute}"

        } else {

            timeS = EtapThreeFragment.time.minute.toString()

        }

        if(EtapThreeFragment.time.hour < 10) {

            timeH = "0${EtapThreeFragment.time.hour}"

        } else {

            timeH = EtapThreeFragment.time.hour.toString()

        }

        time.text = "$timeH : $timeS"
        peoples.text = EtapFourFragment.number.value.toString()

        if(EtapFiveFragment.code.text.isNullOrBlank()){
            EtapFiveFragment.code.setText("48")
        }

        phone.text = "+${EtapFiveFragment.code.text} ${EtapFiveFragment.phone.text}"

        val tabStrip = tabsLayout.getChildAt(0) as LinearLayout
        for (i in 0 until tabStrip.childCount) {
            tabStrip.getChildAt(i).setOnTouchListener { v, event -> false }
        }

        if(check()){

            next.isEnabled = true
            next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
            next.text = "potwierdź rezerwacje"

        } else {

            next.isEnabled = false
            next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
            next.text = "Sprawdź poprawność danych"

        }

    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if(menuVisible){
            update()
        }
    }

    private fun verification(){

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                pin.setText(credential.smsCode)

            }

            override fun onVerificationFailed(e: FirebaseException) {

                if (e is FirebaseAuthInvalidCredentialsException) {

                    nextV.isEnabled = false
                    nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
                    nextV.text = "Nie udało się wysłać kodu"

                } else if (e is FirebaseTooManyRequestsException) {

                    nextV.isEnabled = false
                    nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
                    nextV.text = "System przeciążony"

                }

            }

            override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken) {

                countdown.start()
                verificationid = verificationId!!


            }
        }

        if(!NetworkUtils.isNetworkConnected(this.requireActivity())){

            error.visibility = View.VISIBLE

            nextV.isEnabled = true
            nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorError)
            nextV.text = "Okej, już mam połączenie"

            nextV.setOnClickListener {

                verification()

            }

        } else {

            error.visibility = View.INVISIBLE

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+${phone.text.trim()}",
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                callbacks
            )

            if(pin.text?.trim()?.length != 6) {

                nextV.isEnabled = false
                nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
                nextV.text = "Wpisz kod"

            } else {

                nextV.isEnabled = true
                nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                nextV.text = "Zweryfikuj kod"

            }

            nextV.setOnClickListener {

                pin.isEnabled = false

                backV.visibility = View.INVISIBLE

                nextV.isEnabled = false
                nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
                nextV.text = "Weryfikacja kodu..."

                code = pin.text.toString()

                val credential = PhoneAuthProvider.getCredential(verificationid, code)
                signInWithPhoneAuthCredential(credential)

            }

        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        if(auth.currentUser != null) {
            auth.signOut()
        }

        auth.signInWithCredential(credential).addOnCompleteListener(this.requireActivity()) { task ->

                if (task.isSuccessful) {

                    countdown.stop()

                    val data = ReservationItem(person.text.toString(), Timestamp(EtapTwoFragment.date.time), peoples.text.toString(), phone.text.toString(), commentsEdit.text.toString(), ReservationStatus.WAIT.status, auth.currentUser?.uid!!)

                    FirebaseFirestore.getInstance().collection("rezerwacje").document().set(data).addOnSuccessListener {

                        nextV.isEnabled = true
                        nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorCorrect)
                        nextV.text = "Udało się!, zamknij"

                        nextV.setOnClickListener {

                            this@EtapSixFragment.requireActivity().finish()

                        }

                    }.addOnFailureListener {

                        backV.visibility = View.VISIBLE

                        pin.isEnabled = true

                        nextV.isEnabled = false
                        nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
                        nextV.text = "Wystąpił błąd z bazą danych"

                    }

                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                        backV.visibility = View.VISIBLE

                        pin.text = null

                        pin.isEnabled = true

                        nextV.isEnabled = false
                        nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
                        nextV.text = "Błędny kod"

                    } else {

                        backV.visibility = View.VISIBLE

                        pin.isEnabled = true

                        if(!NetworkUtils.isNetworkConnected(this.requireActivity())){

                            error.visibility = View.VISIBLE

                            nextV.isEnabled = true
                            nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorError)
                            nextV.text = "Okej, już mam połączenie"

                            nextV.setOnClickListener {

                                error.visibility = View.INVISIBLE

                                pin.isEnabled = false

                                backV.visibility = View.INVISIBLE

                                nextV.isEnabled = false
                                nextV.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorPrimaryDark)
                                nextV.text = "Weryfikacja kodu..."

                                code = pin.text.toString()

                                val credential = PhoneAuthProvider.getCredential(verificationid, code)
                                signInWithPhoneAuthCredential(credential)

                            }

                        }

                    }

                }
            }
    }

}
