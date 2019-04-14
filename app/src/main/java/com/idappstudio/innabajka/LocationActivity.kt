package com.idappstudio.innabajka

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_location.*
import com.google.android.gms.maps.CameraUpdateFactory
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.SupportMapFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.MarkerOptions
import com.idappstudio.innabajka.adapters.CustomInfoWindowAdapter

class LocationActivity : FragmentActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    override fun onMyLocationButtonClick(): Boolean {
        return true
    }

    override fun onMyLocationClick(p0: Location) {

    }

    private val FINE_LOCATION_PERMISSION_REQUEST = 1
    private var mMap: GoogleMap? = null
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        toolbar.setNavigationIcon(R.drawable.ic_round_menu_24px)
        toolbar.title = "Lokalizacja"
        toolbar.setNavigationOnClickListener {

            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()

        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        val dialogBuilder = AlertDialog.Builder(this,
            R.style.Base_Theme_MaterialComponents_Dialog
        )

        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_navigate, null)
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorTranspery)))
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.setCancelable(true)

        fabNavigate.setOnClickListener {

            if(!alertDialog.isShowing){
                alertDialog.show()
            }

            if(alertDialog.isShowing){

                val close = alertDialog.findViewById<ImageButton>(R.id.imageButton)
                val method = alertDialog.findViewById<RadioGroup>(R.id.radioGroup)
                val nav = alertDialog.findViewById<Button>(R.id.button3)

                close.setOnClickListener {

                    alertDialog.dismiss()

                }

                nav.setOnClickListener {

                    Log.d("TAG", method.checkedRadioButtonId.toString())

                    val uri = when(method.checkedRadioButtonId) {

                        R.id.walk -> "google.navigation:q=inna+bajka,+Mikołów+Polska&mode=w"
                        R.id.bike -> "google.navigation:q=inna+bajka,+Mikołów+Polska&mode=l"
                        R.id.car -> "google.navigation:q=inna+bajka,+Mikołów+Polska&mode=d"
                        else -> "google.navigation:q=inna+bajka,+Mikołów+Polska"

                    }

                    val gmmIntentUri = Uri.parse(uri)
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)
                    alertDialog.dismiss()

                }

            }

        }

    }

    override fun onMapReady(googleMap: GoogleMap) {

        loading.visibility = View.GONE

        mMap = googleMap

        try {

           googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style
                )
            )

        } catch (e: Resources.NotFoundException) {
            Log.e("MAP STYLE", "Can't find style. Error: ", e)
        }

        findLocation()

    }

    private fun findLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_LOCATION_PERMISSION_REQUEST)

        } else {

            mMap?.isMyLocationEnabled = true

            val destination = LatLng(50.170257, 18.905294)
            val marker = mMap?.addMarker(MarkerOptions()
                .position(destination)
                .title("Inna Bajka")
                .icon(bitmapDescriptorFromVector(applicationContext, R.drawable.ic_icon_map_marker)))

            mMap?.setInfoWindowAdapter(CustomInfoWindowAdapter(this))

            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 15f))
            mMap?.animateCamera(CameraUpdateFactory.zoomIn())
            mMap?.animateCamera(CameraUpdateFactory.zoomTo(19f), 5000, null)
            val cameraPosition = CameraPosition.Builder()
                .target(destination)      // Sets the center of the map to Mountain View
                .zoom(19f)                   // Sets the zoom
                .bearing(275f)                // Sets the orientation of the camera to east
                .tilt(7f)                   // Sets the tilt of the camera to 30 degrees
                .build()                   // Creates a CameraPosition from the builder

            mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            marker?.showInfoWindow()

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            FINE_LOCATION_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    findLocation()
                }
            }
        }
    }

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int) : BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId) as Drawable
        vectorDrawable.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}
