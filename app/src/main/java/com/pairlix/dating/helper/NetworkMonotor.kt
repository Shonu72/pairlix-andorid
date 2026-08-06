package com.pairlix.dating.helper



import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log

class NetworkMonitor(
    private val context: Context,
    private val onConnected: () -> Unit,
    private val onDisconnected: () -> Unit
) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun start() {
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                Log.d("NetworkMonitor", "Internet Connected ✅")
                onConnected()
            }

            override fun onLost(network: Network) {
                Log.d("NetworkMonitor", "Internet Lost ❌")
                onDisconnected()
            }
        })
    }
}
