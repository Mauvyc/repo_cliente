package com.example.serviconnecta.feature.auth.ui.identity

import androidx.compose.foundation.layout.*
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
fun IdentityIntroScreen(
    onStartClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Verificación de identidad", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Completa la verificación para desbloquear todas las funciones.")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onStartClick,
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