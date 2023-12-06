package com.lp.tp1.frontend.screens.switchboard

import android.content.Context
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.common.util.concurrent.ListenableFuture
import com.lp.tp1.backend.BarCodeAnalyser
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Composable
fun SwitchboardScreen(
    navController: NavHostController,
    link: String,
    vm: SwitchboardScreenVM = viewModel()
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState = vm.uiState.collectAsState().value

    LaunchedEffect(uiState.requestedLoading){
        if(!uiState.requestedLoading){ vm.load(link) }
    }

    Column {

        if(!uiState.isLoading){

            Box {
                AndroidView(
                    factory = { androidViewContext ->
                        PreviewView(androidViewContext).apply {
                            this.scaleType = PreviewView.ScaleType.FILL_CENTER
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        }
                    },
                    modifier = Modifier.fillMaxSize(),

                    update = { previewView ->
                        val cameraSelector: CameraSelector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()

                        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                            ProcessCameraProvider.getInstance(context)

                        cameraProviderFuture.addListener({
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }
                            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                            val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageAnalysis
                                )
                            } catch (_: Exception) {
                            }
                        }, ContextCompat.getMainExecutor(context))
                    }
                )
            }
        }
    }
}
