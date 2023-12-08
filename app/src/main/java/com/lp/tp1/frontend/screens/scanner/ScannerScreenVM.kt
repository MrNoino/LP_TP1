package com.lp.tp1.frontend.screens.scanner

import android.app.Application
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.google.common.util.concurrent.ListenableFuture
import com.lp.tp1.LPTP1Application
import com.lp.tp1.backend.repositories.UtilsRepository
import com.lp.tp1.frontend.goToSwitchboard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScannerScreenVM(
    private val application: Application,
    private val utilsRepository: UtilsRepository
): ViewModel() {

    companion object Factory{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as LPTP1Application)
                val utilsRepository = app.container.utilsRepository

                ScannerScreenVM(app,utilsRepository)
            }
        }
    }

    data class UiState(
        val link: String = "",
        val showGoButton: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()



    fun checkQrCode(barCodeValue: String, navController: NavHostController, cameraProvider: ProcessCameraProvider){
        if(barCodeValue.startsWith("https://lp-tp1-api.vercel.app/")){
            _uiState.update {
                uiState.value.copy(
                    showGoButton = true,
                    link = barCodeValue
                )
            }

            cameraProvider.unbindAll()
            navController.goToSwitchboard(barCodeValue)
        }
    }

    fun toggleFlashLight(){
        utilsRepository.toggleFlashLight()
    }
}