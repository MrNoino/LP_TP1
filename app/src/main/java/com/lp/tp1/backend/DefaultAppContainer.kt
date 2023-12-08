package com.lp.tp1.backend

import android.app.Application
import com.lp.tp1.backend.repositories.UtilsRepository

class DefaultAppContainer(application: Application): AppContainer{
    override val utilsRepository: UtilsRepository by lazy {
        UtilsRepository(application)
    }
}