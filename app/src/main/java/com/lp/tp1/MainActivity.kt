package com.lp.tp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lp.tp1.frontend.Routes
import com.lp.tp1.frontend.screens.scanner_screen.ScannerScreen
import com.lp.tp1.frontend.ui.theme.Lp_tp1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lp_tp1Theme {

                val navController = rememberNavController()

                Column{

                    NavHost(
                        navController = navController,
                        startDestination = Routes.Scanner
                    ){
                        composable(Routes.Scanner){
                            ScannerScreen(navController = navController)
                        }

                        composable(Routes.Preview){

                        }
                    }
                }
            }
        }
    }
}