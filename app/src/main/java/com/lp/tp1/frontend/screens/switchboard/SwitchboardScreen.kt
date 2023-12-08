package com.lp.tp1.frontend.screens.switchboard

import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.common.util.concurrent.ListenableFuture
import com.lp.tp1.R
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
    val turnOnFlash = remember { mutableStateOf(false) }

    LaunchedEffect(uiState.requestedLoading) {
        if (!uiState.requestedLoading) {
            vm.load(link)
        }
    }


    if (vm.isNetworkAvailable()) {

        if (!uiState.isLoading) {


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

                    val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

                    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                        ProcessCameraProvider.getInstance(context)

                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                    cameraProviderFuture.addListener({
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

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

                            val imageCapture = ImageCapture.Builder()
                                .setFlashMode(if (turnOnFlash.value) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF)
                                .build()

                            val camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalysis)

                            if(camera.cameraInfo.hasFlashUnit()){
                                camera.cameraControl.enableTorch(turnOnFlash.value)
                            }
                        } catch (_: Exception) {}
                    }, ContextCompat.getMainExecutor(context))
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = {
                        turnOnFlash.value = !turnOnFlash.value
                        Toast.makeText(context, turnOnFlash.value.toString(), Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.flash),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .alpha(0.7f),
                    model = uiState.imageUrl,
                    contentDescription = null
                )
            }

        }
    } else {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.no_wifi),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sem conectividade",
                fontWeight = FontWeight.Medium
            )
        }
    }
}
