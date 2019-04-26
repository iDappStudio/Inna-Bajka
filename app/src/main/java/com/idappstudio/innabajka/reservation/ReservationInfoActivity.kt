package com.idappstudio.innabajka.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.enums.ReservationStatus
import com.idappstudio.innabajka.model.ReservationItem
import kotlinx.android.synthetic.main.activity_reservation_info.*
import java.text.SimpleDateFormat
import android.content.Intent
import android.net.Uri


class ReservationInfoActivity : AppCompatActivity() {

    private lateinit var item: ReservationItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_info)

        item = intent.getParcelableExtra("item")

        val dateFormat = SimpleDateFormat("dd/MM/yyy")
        val dayFormat = SimpleDateFormat("EEEE")
        val timeFormat = SimpleDateFormat("HH:mm")

        personName.text = item.imie
        timeText.text = timeFormat.format(item.data.toDate())
        dataText.text = "${dateFormat.format(item.data.toDate())} • ${dayFormat.format(item.data.toDate())}"
        personNumberText.text = item.miejsc
        phoneText.text = item.telefon

        if(item.uwagi.isNotEmpty()){

            commentsEdit.text = item.uwagi

        } else {

            commentsEdit.text = "Brak dodatkowych uwag"

        }

        btnClose3.setOnClickListener {

            finish()

        }

        if(item.status == ReservationStatus.CANCELED.status || item.status == ReservationStatus.REJECTED.status){

            next2.isEnabled = false
            next2.visibility = View.INVISIBLE

        }

        next2.setOnClickListener {

            next2.isEnabled = false
            FirebaseFirestore.getInstance().collection("rezerwacje").document(item.id)
                .update("status", ReservationStatus.CANCELED.status).addOnSuccessListener {

                    next2.visibility = View.INVISIBLE

                }

        }

        next.setOnClickListener {

            val phone = "+48780140640"
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)

        }

    }

}
