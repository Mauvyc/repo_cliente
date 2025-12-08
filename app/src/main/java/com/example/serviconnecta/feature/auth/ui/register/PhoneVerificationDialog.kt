package com.example.serviconnecta.feature.auth.ui.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.serviconnecta.ui.theme.ServiBlue

@Composable
fun PhoneVerificationDialog(
    uiState: RegistrationUiState,
    onCodeChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirmClick,
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ServiBlue
                )
            ) {
                Text(text = "Verificar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(text = "Cancelar")
            }
        },
        title = {
            Text(text = "Verifica tu teléfono")
        },
        text = {
            Column {
                Text(
                    text = "Hemos enviado un código de 4 dígitos a ${uiState.maskedPhone ?: uiState.phoneNumber}"
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = uiState.verificationCode,
                    onValueChange = { if (it.length <= 4) onCodeChange(it) },
                    label = { Text("Código de verificación") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                uiState.errorMessage?.let {
                    Text(text = it)
                }
            }
        }
    )
}