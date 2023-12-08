package com.lp.tp1.backend.repositories

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraManager
import android.util.Log
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UtilsRepository(private val application: Application) {

    private var flashLightOn = false



    fun toggleFlashLight() {


        /*
        try {
            flashLightOn = if (!flashLightOn) {
                cameraManager.setTorchMode(cameraId, true)
                true
            } else {
                cameraManager.setTorchMode(cameraId, false)
                false
            }
        } catch (e: Exception) {
            Log.e("Flashlight error", e.message.toString())
        }

         */
    }
}