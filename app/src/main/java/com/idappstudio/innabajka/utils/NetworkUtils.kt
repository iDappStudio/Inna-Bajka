package com.idappstudio.innabajka.utils

import android.content.Context
import android.net.NetworkInfo
import android.net.ConnectivityManager

object NetworkUtils {

    private val TYPE_WIFI = 1
    private val TYPE_MOBILE = 2
    private val TYPE_NOT_CONNECTED = 0

    fun getConnectivityStatus(context: Context): Int {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo != null) {
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI && networkInfo.state == NetworkInfo.State.CONNECTED) {

                return TYPE_WIFI

            } else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE && networkInfo.state == NetworkInfo.State.CONNECTED) {
                return TYPE_MOBILE
            }
        }
        return TYPE_NOT_CONNECTED
    }

    fun isNetworkConnected(context: Context): Boolean {
        val networkStatus = getConnectivityStatus(context)
        return networkStatus == TYPE_WIFI || networkStatus == TYPE_MOBILE
    }

}