package com.pairlix.dating.helper

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.annotation.RequiresPermission
import javax.inject.Inject

class Network @Inject constructor(val context: Context) : NetworkConnectivity {
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun getNetworkInfo(): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun isConnected(): Boolean {
        val info = getNetworkInfo()
        return info != null && info.isConnected
    }
}

interface NetworkConnectivity {
    fun getNetworkInfo(): NetworkInfo?
    fun isConnected(): Boolean
}