package com.pairlix.dating.helper

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission


object InternetConnection {
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun checkConnection(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connMgr != null) {
            val activeNetworkInfo = connMgr.activeNetworkInfo
            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                return if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    true
                } else activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
            }
        }
        return false
    }
}