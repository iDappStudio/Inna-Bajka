package com.idappstudio.innabajka

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.title = "Menu"
        toolbar.setNavigationOnClickListener {

            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()

        }

        val text = include.findViewById<TextView>(R.id.head_title_text)
        val text2 = include6.findViewById<TextView>(R.id.head_title_text)

        text.text = "jedzenie"
        text2.text = "napoje"

    }
}
