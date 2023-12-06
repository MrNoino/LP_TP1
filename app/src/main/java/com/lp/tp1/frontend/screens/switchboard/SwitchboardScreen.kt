package com.lp.tp1.frontend.screens.switchboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun SwitchboardScreen(
    navController: NavHostController,
    link: String
) {

    Text(text = link)
}