package com.dn0ne.banking

import android.app.Application
import com.dn0ne.banking.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BankingApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BankingApp)
            modules(appModule)
        }
    }
}