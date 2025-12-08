package com.example.serviconnecta.core.network

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NoInternetDialog() {
    AlertDialog(
        onDismissRequest = { /* no se puede cerrar */ },
        title = { Text(text = "Sin conexión a internet") },
        text = {
            Column {
                Text(
                    text = "No se detecta conexión a internet.\n\n" +
                            "Revisa tu conexión Wi-Fi o datos móviles " +
                            "para continuar usando ServiConecta.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        confirmButton = { /* sin botón para que sea bloqueante */ }
    )
}