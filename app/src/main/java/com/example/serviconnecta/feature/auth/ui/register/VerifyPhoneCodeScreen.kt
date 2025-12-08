package com.example.serviconnecta.feature.auth.ui.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serviconnecta.ui.theme.ServiBlue

@Composable
fun VerifyPhoneCodeScreen(
    uiState: RegistrationUiState,
    onCodeChange: (String) -> Unit,
    onVerifyClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Por favor, revisa tu teléfono", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Hemos enviado un código de 4 dígitos a ${uiState.maskedPhone ?: uiState.phoneNumber}")

                Spacer(modifier = Modifier.height(16.dp))

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
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Button(
                    onClick = onVerifyClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ServiBlue
                    ),
                    enabled = !uiState.isLoading
                ) {
                    Text(text = "Verificar")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onCancelClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Text(text = "Cancelar")
                }
            }
        }
    }
}
