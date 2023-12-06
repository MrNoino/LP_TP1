package com.lp.tp1.backend

class DefaultAppContainer: AppContainer{
    override val defaultAppContainer: DefaultAppContainer by lazy {
        DefaultAppContainer()
    }
}