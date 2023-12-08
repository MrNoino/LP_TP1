package com.lp.tp1

import android.app.Application
import com.lp.tp1.backend.AppContainer
import com.lp.tp1.backend.DefaultAppContainer

class LPTP1Application: Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        container = DefaultAppContainer(this)
    }
}