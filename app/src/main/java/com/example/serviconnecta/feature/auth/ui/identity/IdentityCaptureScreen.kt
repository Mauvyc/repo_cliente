package com.example.serviconnecta.feature.auth.ui.identity

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.serviconnecta.ui.theme.ServiBlue
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.File

@Composable
fun IdentityCaptureScreen(
    uiState: IdentityUiState,
    onPhotoCaptured: (String) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember { mutableStateOf(false) }
    var isCapturing by remember { mutableStateOf(false) }

    // Para el diálogo de vista previa
    var previewBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showPreviewDialog by remember { mutableStateOf(false) }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    // Launcher para pedir permiso de cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            hasCameraPermission = true
        } else {
            // Si el usuario no da permiso, volvemos a la pantalla previa
            onCancel()
        }
    }

    // Pedimos permiso la primera vez que entra en la pantalla
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // Si todavía no hay permiso, mostramos loader
    if (!hasCameraPermission) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // ---------- UI con preview de cámara ----------
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Preview de la cámara
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)

                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also { p ->
                        p.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageCapture
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            }
        )

        // Overlay inferior con botón
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (!isCapturing) {
                        isCapturing = true

                        val photoFile = File(
                            context.cacheDir,
                            "identity-${System.currentTimeMillis()}.jpg"
                        )

                        val outputOptions =
                            ImageCapture.OutputFileOptions.Builder(photoFile).build()

                        imageCapture.takePicture(
                            outputOptions,
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(
                                    output: ImageCapture.OutputFileResults
                                ) {
                                    try {
                                        // Leemos la foto del archivo
                                        val bytes = photoFile.readBytes()
                                        var bitmap = BitmapFactory.decodeByteArray(
                                            bytes,
                                            0,
                                            bytes.size
                                        )

                                        // --- Corregir orientación usando EXIF ---
                                        val exif = ExifInterface(photoFile.path)
                                        val orientation = exif.getAttributeInt(
                                            ExifInterface.TAG_ORIENTATION,
                                            ExifInterface.ORIENTATION_UNDEFINED
                                        )

                                        val rotationDegrees = when (orientation) {
                                            ExifInterface.ORIENTATION_ROTATE_90 -> 90
                                            ExifInterface.ORIENTATION_ROTATE_180 -> 180
                                            ExifInterface.ORIENTATION_ROTATE_270 -> 270
                                            else -> 0
                                        }

                                        if (rotationDegrees != 0) {
                                            val matrix = Matrix().apply {
                                                postRotate(rotationDegrees.toFloat())
                                            }
                                            bitmap = Bitmap.createBitmap(
                                                bitmap,
                                                0,
                                                0,
                                                bitmap.width,
                                                bitmap.height,
                                                matrix,
                                                true
                                            )
                                        }
                                        // ----------------------------------------

                                        // Mostramos el bitmap corregido en el diálogo
                                        previewBitmap = bitmap
                                        showPreviewDialog = true
                                        isCapturing = false
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        isCapturing = false
                                    }
                                }


                                override fun onError(exception: ImageCaptureException) {
                                    isCapturing = false
                                }
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isCapturing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ServiBlue
                )
            ) {
                Text(text = if (isCapturing) "Procesando..." else "Tomar foto")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onCancel) {
                Text(text = "Cancelar")
            }
        }
    }

    // ---------- Dialog de vista previa ----------
    if (showPreviewDialog && previewBitmap != null) {
        AlertDialog(
            onDismissRequest = { showPreviewDialog = false },
            title = { Text(text = "Vista previa del documento") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        bitmap = previewBitmap!!.asImageBitmap(),
                        contentDescription = "Foto del documento",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "¿Quieres enviar esta foto o tomar otra?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // 👉 Como es simulación, enviamos una imagen muy pequeña (dummy) al backend
                        val dummyBytes = "test-image".toByteArray()
                        val dummyBase64 =
                            Base64.encodeToString(dummyBytes, Base64.NO_WRAP)

                        showPreviewDialog = false
                        onPhotoCaptured(dummyBase64)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ServiBlue
                    )
                ) {
                    Text("Enviar foto")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Cerrar el dialog y permitir otra captura
                        showPreviewDialog = false
                        previewBitmap = null
                    }
                ) {
                    Text("Tomar otra")
                }
            }
        )
    }
}
