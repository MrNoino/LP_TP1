package com.lp.tp1.frontend

import androidx.navigation.NavController

object Routes{
    const val Scanner = "scanner"
    const val Preview = "preview"
}

fun NavController.goBack(){
    this.navigateUp()
}

fun NavController.goToPreview(){
    this.navigate(Routes.Preview)
}