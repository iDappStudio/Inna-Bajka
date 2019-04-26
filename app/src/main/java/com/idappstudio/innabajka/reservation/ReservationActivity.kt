package com.idappstudio.innabajka.reservation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.idappstudio.innabajka.NavigationActivity
import com.idappstudio.innabajka.R
import com.idappstudio.innabajka.adapters.HeaderItem
import com.idappstudio.innabajka.adapters.ReservationItemAdapater
import com.idappstudio.innabajka.enums.ReservationStatus
import com.idappstudio.innabajka.model.ReservationItem
import com.ravikoradiya.library.CenterTitle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
import kotlinx.android.synthetic.main.activity_reservation.*

class ReservationActivity : AppCompatActivity() {

    private lateinit var db: CollectionReference

    private var listener: ListenerRegistration? = null

    private val adapter = GroupAdapter<ViewHolder>()

    private val confirmed = Section()
    private val wait = Section()
    private val rejected = Section()
    private val canceled = Section()

    private val confirmedItems = HashMap<String, ReservationItemAdapater>()
    private val waitItems = HashMap<String, ReservationItemAdapater>()
    private val rejectedItems = HashMap<String, ReservationItemAdapater>()
    private val canceledItems = HashMap<String, ReservationItemAdapater>()

    private fun addItem(item: ReservationItem) {

        if(item.status == ReservationStatus.CONFIRMED.status) {
            confirmedItems[item.id] = ReservationItemAdapater(item, this)
            confirmed.update(confirmedItems.values)
        }

        if(item.status == ReservationStatus.WAIT.status) {
            waitItems[item.id] = ReservationItemAdapater(item, this)
            wait.update(waitItems.values)
        }

        if(item.status == ReservationStatus.REJECTED.status) {
            rejectedItems[item.id] = ReservationItemAdapater(item, this)
            rejected.update(rejectedItems.values)
        }

        if(item.status == ReservationStatus.CANCELED.status) {
            canceledItems[item.id] = ReservationItemAdapater(item, this)
            canceled.update(canceledItems.values)
        }

    }

    private fun deleteItem(item: ReservationItem) {

        if(item.status == ReservationStatus.CONFIRMED.status) {
            confirmedItems.remove(item.id)
            confirmed.update(confirmedItems.values)
        }

        if(item.status == ReservationStatus.WAIT.status) {
            waitItems.remove(item.id)
            wait.update(waitItems.values)
        }

        if(item.status == ReservationStatus.REJECTED.status) {
            rejectedItems.remove(item.id)
            rejected.update(rejectedItems.values)
        }

        if(item.status == ReservationStatus.CANCELED.status) {
            canceledItems.remove(item.id)
            canceled.update(canceledItems.values)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)
        setSupportActionBar(toolbar)

        CenterTitle.centerTitle(toolbar,true)

        toolbar.setTitleTextAppearance(this, R.style.TitleToolBar)

        toolbar.setNavigationIcon(R.drawable.ic_round_menu_24px)
        toolbar.setNavigationOnClickListener {

            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()

        }

        fabReservation.setOnClickListener {

            val intent = Intent(this, SetReservationActivity::class.java)
            startActivity(intent)

        }

        supportActionBar?.title = "Twoje rezerwacje"

        db = FirebaseFirestore.getInstance().collection("rezerwacje")

        initRV {

            rv.itemAnimator = SlideInDownAnimator()
            rv.adapter = adapter

            getItems {

                loading.visibility = View.GONE

            }

        }

    }

    private fun initRV(onComplete: () -> Unit){

        val rvLLM = LinearLayoutManager(this)
        rvLLM.orientation = RecyclerView.VERTICAL
        rv.layoutManager = rvLLM

        confirmed.setHeader(HeaderItem("Zatwierdzone"))
        confirmed.setHideWhenEmpty(true)
        adapter.add(confirmed)

        wait.setHeader(HeaderItem("Oczekujące"))
        wait.setHideWhenEmpty(true)
        adapter.add(wait)

        rejected.setHeader(HeaderItem("Odrzucone"))
        rejected.setHideWhenEmpty(true)
        adapter.add(rejected)

        canceled.setHeader(HeaderItem("Anulowane"))
        canceled.setHideWhenEmpty(true)
        adapter.add(canceled)

        onComplete()

    }

    private fun getItems(onComplete: () -> Unit){

        if(FirebaseAuth.getInstance().currentUser == null){
            backText.visibility = View.VISIBLE
            onComplete()
            return
        }

        if(listener != null){
            listener?.remove()
        }

        listener = db.whereEqualTo("id", FirebaseAuth.getInstance().currentUser?.uid.toString()).addSnapshotListener(EventListener<QuerySnapshot> { snapshots, e ->

            if (e != null) {
                return@EventListener
            }

            if(snapshots == null){
                return@EventListener
            }

            if(snapshots.isEmpty){
                backText.visibility = View.VISIBLE
                onComplete()
            } else {

                backText.visibility = View.GONE

            }

            for (dc in snapshots.documentChanges) {

                when (dc.type) {

                    DocumentChange.Type.ADDED -> {

                        val item = ReservationItem(
                            dc.document.getString("imie")!!,
                            dc.document.getTimestamp("data")!!,
                            dc.document.getString("miejsc")!!,
                            dc.document.getString("telefon")!!,
                            dc.document.getString("uwagi")!!,
                            dc.document.getString("status")!!,
                            dc.document.id
                        )

                        addItem(item)

                    }

                    DocumentChange.Type.MODIFIED -> {

                        val item = ReservationItem(
                            dc.document.getString("imie")!!,
                            dc.document.getTimestamp("data")!!,
                            dc.document.getString("miejsc")!!,
                            dc.document.getString("telefon")!!,
                            dc.document.getString("uwagi")!!,
                            dc.document.getString("status")!!,
                            dc.document.id
                        )

                        if(confirmedItems.containsKey(item.id)){
                            confirmedItems.remove(item.id)
                            confirmed.update(confirmedItems.values)
                        }

                        if(waitItems.containsKey(item.id)){
                            waitItems.remove(item.id)
                            wait.update(waitItems.values)
                        }

                        if(rejectedItems.containsKey(item.id)){
                            rejectedItems.remove(item.id)
                            rejected.update(rejectedItems.values)
                        }

                        if(canceledItems.containsKey(item.id)){
                            canceledItems.remove(item.id)
                            canceled.update(canceledItems.values)
                        }

                        addItem(item)

                    }

                    DocumentChange.Type.REMOVED -> {

                        val item = ReservationItem(
                            dc.document.getString("imie")!!,
                            dc.document.getTimestamp("data")!!,
                            dc.document.getString("miejsc")!!,
                            dc.document.getString("telefon")!!,
                            dc.document.getString("uwagi")!!,
                            dc.document.getString("status")!!,
                            dc.document.id
                        )

                        deleteItem(item)

                    }

                }

            }

            onComplete()

        })

    }

    override fun onResume() {
        super.onResume()

        getItems {

            loading.visibility = View.GONE

        }

    }

    override fun onDestroy() {
        super.onDestroy()

        if(listener != null){
            listener?.remove()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()

    }

}
