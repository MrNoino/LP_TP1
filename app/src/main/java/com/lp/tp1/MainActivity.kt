package com.lp.tp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lp.tp1.frontend.Routes
import com.lp.tp1.frontend.screens.scanner.ScannerScreen
import com.lp.tp1.frontend.screens.switchboard.SwitchboardScreen
import com.lp.tp1.frontend.ui.theme.Lp_tp1Theme
import java.net.URLDecoder

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

                        composable(
                            "${Routes.Switchboard}/{link}",
                            arguments = listOf(
                                navArgument("link"){type = NavType.StringType}
                            )
                        ){backstack->

                            val link = URLDecoder.decode(backstack.arguments?.getString("link") ?: "", "UTF-8")

                            SwitchboardScreen(
                                navController = navController,
                                link = link
                            )
                        }
                    }
                }
            }
        }
    }
}

//TODO: Request,