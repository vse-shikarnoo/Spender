package com.example.spender.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

object InternetChecker {
    fun check(appContext: Application): Boolean {
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}
