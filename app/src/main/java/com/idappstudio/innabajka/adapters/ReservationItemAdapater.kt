package com.idappstudio.innabajka.adapters

import android.content.Context
import android.content.Intent
import com.google.firebase.firestore.FirebaseFirestore
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.enums.ReservationStatus
import com.idappstudio.innabajka.model.ReservationItem
import com.idappstudio.innabajka.reservation.ReservationInfoActivity
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.list_item.date
import kotlinx.android.synthetic.main.list_item.time
import kotlinx.android.synthetic.main.list_item3.*
import java.text.SimpleDateFormat

class ReservationItemAdapater(private val item: ReservationItem, private val context: Context) : Item() {

    override fun bind(holder: ViewHolder, position: Int) {

        val intent = Intent(context, ReservationInfoActivity::class.java)
        intent.putExtra("item", item)

        val dateFormat = SimpleDateFormat("dd/MM/yyy")
        val dayFormat = SimpleDateFormat("EEEE")
        val timeFormat = SimpleDateFormat("HH:mm")

        holder.date.text = "${dateFormat.format(item.data.toDate())}"
        holder.time.text = "${timeFormat.format(item.data.toDate())} • ${dayFormat.format(item.data.toDate())}"

        holder.info.setOnClickListener {

            context.startActivity(intent)

        }

        if(item.status != ReservationStatus.CANCELED.status || item.status != ReservationStatus.REJECTED.status){

            holder.layout.setOnLongClickListener {

                if(holder.info.tag == "yes"){

                    holder.info.setImageDrawable(context.resources.getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp))
                    holder.layout.background = context.resources.getDrawable(R.drawable.reservation_background)

                    holder.info.setOnClickListener {

                        context.startActivity(intent)

                    }

                    holder.info.tag = "no"

                } else {

                    holder.info.setImageDrawable(context.resources.getDrawable(R.drawable.ic_delete_forever_black_24dp))
                    holder.layout.background = context.resources.getDrawable(R.drawable.delete_mode_reservation_background)
                    holder.info.setOnClickListener {

                        FirebaseFirestore.getInstance().collection("rezerwacje").document(item.id)
                            .update("status", ReservationStatus.CANCELED.status)

                    }

                    holder.info.tag = "yes"

                }

                true

            }

        }

    }

    override fun getLayout(): Int = R.layout.list_item3

}