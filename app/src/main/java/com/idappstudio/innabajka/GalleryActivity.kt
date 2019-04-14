package com.idappstudio.innabajka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_gallery.*


class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.ic_round_menu_24px)
        toolbar.setNavigationOnClickListener {

            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()

        }

        supportActionBar?.title = "Galeria"

    }

}
