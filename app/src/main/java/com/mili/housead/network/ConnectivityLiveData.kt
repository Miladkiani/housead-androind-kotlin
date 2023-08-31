package com.mili.housead.network

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData

object ConnectivityLiveData : LiveData<Boolean>(){

    private var broadcastReceiver: BroadcastReceiver? = null
    private lateinit var application: Application
    private lateinit var connectivityManager: ConnectivityManager

    private fun setConnectivityManager(){
        connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun init(application: Application){
        this.application = application
        setConnectivityManager()
    }

    fun isInternetOn(): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    private val networkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object: ConnectivityManager.NetworkCallback(){

        override fun onAvailable(network: Network?) {
            postValue(true)
        }

        override fun onUnavailable() {
            postValue(false)
        }
    }

    override fun onActive() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            registerBroadCastReceiver()
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        }else{
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    override fun onInactive() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            unRegisterBroadCastReceiver()
        }else{
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    private fun registerBroadCastReceiver() {
        if (broadcastReceiver == null) {
            val filter = IntentFilter()
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(_context: Context, intent: Intent) {
                    val extras = intent.extras
                    val info = extras?.getParcelable<NetworkInfo>("networkInfo")
                    value = info?.state == NetworkInfo.State.CONNECTED
                }
            }

            application.registerReceiver(broadcastReceiver, filter)
        }
    }

    private fun unRegisterBroadCastReceiver() {
        if (broadcastReceiver != null) {
            application.unregisterReceiver(broadcastReceiver)
            broadcastReceiver = null
        }
    }
}