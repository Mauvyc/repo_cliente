package com.example.serviconnecta.feature.shared.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel,
    onNavigateBack: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    val isLoading = viewModel.isLoading
    val backendError = viewModel.errorMessage
    val successMessage = viewModel.successMessage

    val context = LocalContext.current

    // Mostrar Toast en error del backend
    LaunchedEffect(backendError) {
        backendError?.let {
            localError = it
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    // Mostrar Toast en éxito y volver atrás
    LaunchedEffect(successMessage) {
        successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            delay(1500)
            viewModel.clearMessages()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cambiar contraseña") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Cambio de Contraseña",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                "Por seguridad, ingresa tu contraseña actual y luego tu nueva contraseña.",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = currentPassword,
                onValueChange = {
                    currentPassword = it
                    localError = null
                },
                label = { Text("Contraseña actual") },
                placeholder = { Text("Ingresa tu contraseña actual") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = localError != null
            )

            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    localError = null
                },
                label = { Text("Nueva contraseña") },
                placeholder = { Text("Mínimo 6 caracteres") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = localError != null
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    localError = null
                },
                label = { Text("Confirmar nueva contraseña") },
                placeholder = { Text("Repite tu nueva contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = localError != null
            )

            localError?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    when {
                        currentPassword.isBlank() -> {
                            localError = "Ingresa tu contraseña actual"
                        }
                        newPassword.isBlank() -> {
                            localError = "Ingresa tu nueva contraseña"
                        }
                        newPassword.length < 6 -> {
                            localError = "La contraseña debe tener al menos 6 caracteres"
                        }
                        newPassword != confirmPassword -> {
                            localError = "Las contraseñas no coinciden"
                        }
                        currentPassword == newPassword -> {
                            localError = "La nueva contraseña debe ser diferente a la actual"
                        }
                        else -> {
                            viewModel.changePassword(currentPassword, newPassword)
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp
                    )
                }
                Text("Actualizar Contraseña")
            }

            TextButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}