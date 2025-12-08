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
fun IdentityInstructionsScreen(
    onTakePhotoClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Antes de tomar la foto", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "• Asegúrate de que tu documento sea válido.")
            Text(text = "• Coloca el documento en una superficie plana.")
            Text(text = "• Evita reflejos y fotos borrosas.")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onTakePhotoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ServiBlue
                )
            ) {
                Text(text = "Tomar foto")
            }
        }
    }
}