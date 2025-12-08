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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serviconnecta.ui.theme.ServiBlue

@Composable
fun RegisterDataInfoScreen(
    onConfirmClick: () -> Unit,
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
                Text(text = "Datos a proporcionar", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Los siguientes son los datos que te vamos a pedir:")

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "• Información personal (nombre, género, fecha de nacimiento)")
                Text(text = "• Documentos (DNI, pasaporte, etc.)")
                Text(text = "• Certificados (opcional)")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onConfirmClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ServiBlue
                    )
                ) {
                    Text(text = "Confirmar")
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
