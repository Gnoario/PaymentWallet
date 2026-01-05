package com.app.paymentwallet

import android.app.Application
import com.app.paymentwallet.framework.di.AppGraph

class PaymentWalletApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppGraph.init(this)
    }
}