package com.lp.tp1.frontend.screens.scanner_screen

import androidx.lifecycle.ViewModel
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

    fun checkQrCode(barCodeValue: String){
        if(barCodeValue.startsWith("http://en.m.wikipedia.org")){
            _uiState.update {
                uiState.value.copy(
                    showGoButton = true,
                    link = barCodeValue
                )
            }
        }
    }
}