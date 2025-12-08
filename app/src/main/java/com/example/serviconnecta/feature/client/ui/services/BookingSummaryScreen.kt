package com.example.serviconnecta.feature.client.ui.services

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.serviconnecta.core.ui.components.RatingBar
import com.example.serviconnecta.core.utils.FormatUtils
import com.example.serviconnecta.feature.client.domain.model.Location
import com.example.serviconnecta.feature.client.ui.services.BookingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSummaryScreen(
    serviceId: String,
    onNavigateBack: () -> Unit,
    onNavigateToSelectLocation: () -> Unit,
    onNavigateToDateTime: (String) -> Unit,
    viewModel: BookingViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(serviceId) {
        viewModel.loadServiceDetail(serviceId)
        viewModel.loadLocations()
    }

    // Si ya hay locations y aún no hay una seleccionada en el VM,
    // seleccionamos la default o la primera.
    LaunchedEffect(uiState.locations) {
        if (uiState.selectedLocationId == null && uiState.locations.isNotEmpty()) {
            val defaultLocation = uiState.locations.firstOrNull { it.isDefault }
                ?: uiState.locations.first()
            viewModel.selectLocation(defaultLocation.id)
        }
    }

    val service = uiState.service
    val selectedLocation =
        uiState.locations.firstOrNull { it.id == uiState.selectedLocationId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen de la reserva") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            if (service != null) {
                Surface(
                    tonalElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Total",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                FormatUtils.formatPrice(uiState.total),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Button(
                            onClick = { onNavigateToDateTime(serviceId) },
                            enabled = selectedLocation != null && !uiState.isLoading
                        ) {
                            Text("Seleccionar espacio")
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (uiState.isLoading && service == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (service == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No se pudo cargar el servicio")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Tarjeta del servicio
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                        ) {
                            if (service.imageUrl != null) {
                                AsyncImage(
                                    model = service.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.LightGray)
                                ) {
                                    Icon(
                                        Icons.Default.Image,
                                        contentDescription = null,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                service.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RatingBar(rating = service.rating, starSize = 14)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "(${FormatUtils.formatRating(service.rating)})",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                FormatUtils.formatPrice(service.price),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Resumen de precios
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                FormatUtils.formatPrice(uiState.subtotal),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Descuento", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                FormatUtils.formatPrice(uiState.discount),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Total",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                FormatUtils.formatPrice(uiState.total),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Dirección seleccionada
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Dirección", fontWeight = FontWeight.Bold)
                            Text(
                                selectedLocation?.label ?: "Sin ubicación",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (selectedLocation == null) Color.Gray else Color.Unspecified
                            )
                            if (selectedLocation != null) {
                                Text(
                                    selectedLocation.address,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                        TextButton(onClick = onNavigateToSelectLocation) {
                            Icon(Icons.Default.LocationOn, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (selectedLocation == null) "Agregar" else "Cambiar")
                        }
                    }
                }
            }
        }
    }
}