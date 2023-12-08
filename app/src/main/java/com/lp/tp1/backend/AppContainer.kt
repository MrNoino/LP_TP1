package com.lp.tp1.backend

import com.lp.tp1.backend.repositories.UtilsRepository

interface AppContainer {
    val utilsRepository: UtilsRepository
}