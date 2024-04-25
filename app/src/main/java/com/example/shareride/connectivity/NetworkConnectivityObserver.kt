package com.example.shareride.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


class NetworkConnectivityObserver (private val context: Context) : ConnectivityObserver {

    override fun observe(): Flow<ConnectivityObserver.Status> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            observeNetworkCallback()
        } else {
            observeLegacyConnectivity()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun observeNetworkCallback():Flow<ConnectivityObserver.Status> = callbackFlow {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(ConnectivityObserver.Status.Avalilable).isSuccess
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                trySend(ConnectivityObserver.Status.Avalilable).isSuccess
            }

            override fun onLost(network: Network) {
                trySend(ConnectivityObserver.Status.Lost).isSuccess
            }

            override fun onUnavailable() {
                trySend(ConnectivityObserver.Status.Lost).isSuccess
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }

    }

    private fun observeLegacyConnectivity(): Flow<ConnectivityObserver.Status> = callbackFlow {
        val connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager.activeNetworkInfo
                val isConnected = networkInfo != null && networkInfo.isConnected

                if (isConnected) {
                    trySend(ConnectivityObserver.Status.Avalilable).isSuccess
                } else {
                    trySend(ConnectivityObserver.Status.Unavailable).isSuccess
                }
            }
        }

        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(connectivityReceiver, intentFilter)

        awaitClose { context.unregisterReceiver(connectivityReceiver) }
    }

}