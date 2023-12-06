package com.lp.tp1.frontend

import androidx.navigation.NavController
import java.net.URLEncoder

object Routes{
    const val Scanner = "scanner"
    const val Switchboard = "switchboard"
}

fun NavController.goBack(){
    this.navigateUp()
}

fun NavController.goToSwitchboard(link: String){
    this.navigate("${Routes.Switchboard}/${URLEncoder.encode(link, "UTF-8")}")
}