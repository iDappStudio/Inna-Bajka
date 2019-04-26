@file:Suppress("DEPRECATION")

package com.idappstudio.innabajka

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_navigation.*
import com.fujiyuu75.sequent.Animation
import com.fujiyuu75.sequent.Sequent
import com.idappstudio.innabajka.menu.MenuActivity
import com.idappstudio.innabajka.reservation.ReservationActivity
import com.idappstudio.innabajka.tip.TipActivity

class NavigationActivity : AppCompatActivity() {

    private lateinit var alertDialog: AlertDialog


    @SuppressLint("InflateParams", "PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        Sequent
            .origin(navigationLayout)
            .anim(this, Animation.BOUNCE_IN)
            .start()

        val dialogBuilder = AlertDialog.Builder(
            this,
            R.style.Base_Theme_MaterialComponents_Dialog
        )

        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_tip, null)
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorTranspery)))
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)

        btnClose.setOnClickListener {
            finish()
        }

        btnMenu.setOnClickListener {

            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()

        }

        btnGallery.setOnClickListener {

            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
            finish()

        }

        btnLocation.setOnClickListener {

            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
            finish()

        }

        btnReservation.setOnClickListener {

            val intent = Intent(this, ReservationActivity::class.java)
            startActivity(intent)
            finish()

        }

        button.setOnClickListener {

            val intent = Intent(this, TipActivity::class.java)
            startActivity(intent)

        }

    }

}
