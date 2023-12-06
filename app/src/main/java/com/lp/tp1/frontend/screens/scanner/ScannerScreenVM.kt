package com.lp.tp1.frontend.screens.scanner

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.lp.tp1.frontend.goToSwitchboard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScannerScreenVM: ViewModel() {

    data class UiState(
        val link: String = "",
        val showGoButton: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()


    fun checkQrCode(barCodeValue: String, navController: NavHostController){
        if(barCodeValue.startsWith("https://lp-tp1-api.vercel.app/")){
            _uiState.update {
                uiState.value.copy(
                    showGoButton = true,
                    link = barCodeValue
                )
            }

            navController.goToSwitchboard(barCodeValue)
        }
    }
}