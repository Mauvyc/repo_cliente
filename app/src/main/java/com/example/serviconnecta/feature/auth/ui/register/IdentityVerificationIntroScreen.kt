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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serviconnecta.ui.theme.ServiBlue

@Composable
fun IdentityVerificationIntroScreen(
    onStartVerificationClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Aquí puedes poner una ilustración
            Text(text = "Verificación de identidad", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Completa la verificación de documentos para aumentar la seguridad y proteger tu cuenta.")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onStartVerificationClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ServiBlue
                )
            ) {
                Text(text = "Obtener verificación")
            }
        }
    }
}
