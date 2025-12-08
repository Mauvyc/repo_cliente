package com.example.serviconnecta.feature.worker.ui.requests

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serviconnecta.feature.worker.domain.model.ServiceRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: RequestsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitudes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateBack,
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, null) },
                    label = { Text("Solicitudes") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToProfile,
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.requests) { request ->
                RequestCard(
                    request = request,
                    onClick = { viewModel.showRequestDetail(request) }
                )
            }

            item {
                if (uiState.requests.isEmpty()) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.AutoMirrored.Filled.List, null, modifier = Modifier.size(64.dp), tint = Color.Gray)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("No tienes solicitudes", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }

    if (uiState.showDetailDialog && uiState.selectedRequest != null) {
        RequestDetailDialog(
            request = uiState.selectedRequest!!,
            onDismiss = viewModel::hideRequestDetail,
            onAccept = { viewModel.acceptRequest(uiState.selectedRequest!!.requestId) },
            onReject = { viewModel.rejectRequest(uiState.selectedRequest!!.requestId) }
        )
    }

    uiState.successMessage?.let { message ->
        LaunchedEffect(message) {
            kotlinx.coroutines.delay(2000)
            viewModel.clearMessages()
        }
        Snackbar(modifier = Modifier.padding(16.dp)) {
            Text(message)
        }
    }
}

@Composable
private fun RequestCard(
    request: ServiceRequest,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Person, null, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                // Actualizado: serviceTitle en lugar de serviceName
                Text(request.serviceTitle, fontWeight = FontWeight.Bold)
                Text(
                    "${request.clientName} • ${request.location}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
        }
    }
}

@Composable
private fun RequestDetailDialog(
    request: ServiceRequest,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .width(40.dp)
                            .padding(bottom = 16.dp),
                        thickness = 4.dp,
                        color = Color.Gray
                    )
                }

                Text(request.clientName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                Icon(Icons.Default.Person, null, modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                InfoRow("Fecha:", request.date)
                // Actualizado: timeRange en lugar de time
                InfoRow("Horario:", request.timeRange)
                InfoRow("Ubicación:", request.location)
                
                // El nuevo modelo ServiceRequest NO tiene paymentMethod, así que lo eliminamos o ponemos algo por defecto
                // InfoRow("Método de pago:", request.paymentMethod) 
                // Para evitar romper la UI, simplemente no mostramos esa fila o ponemos texto fijo si es necesario.
                // La quitaré del diálogo ya que no existe en el modelo.

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onAccept,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Aceptar")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Rechazar", color = Color.Red)
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
        Text(value)
    }
}