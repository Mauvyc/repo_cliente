package com.example.serviconnecta.feature.client.ui.reservations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serviconnecta.core.utils.DateTimeUtils
import com.example.serviconnecta.core.utils.FormatUtils
import com.example.serviconnecta.feature.client.domain.model.Booking
import com.example.serviconnecta.feature.client.domain.model.BookingStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReservationsScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToReview: (String) -> Unit,
    viewModel: ReservationsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Refrescar las reservas cada vez que la pantalla se vuelve visible (onResume)
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.loadBookings()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis reservas") }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToHome,
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.List, null) },
                    label = { Text("Reservas") }
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
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Error, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(uiState.error ?: "Error", color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.refresh() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            uiState.bookings.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(Icons.Default.List, null, modifier = Modifier.size(64.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No tienes reservas",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Cuando reserves un servicio aparecerá aquí",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Mis Reservas",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${uiState.bookings.size} reservas",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }

                    items(uiState.bookings) { booking ->
                        BookingCard(
                            booking = booking,
                            onReview = { onNavigateToReview(booking.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingCard(
    booking: Booking,
    onReview: () -> Unit
) {
    // El estado visual es el que viene del backend
    val displayStatus = booking.status

    // Permitir calificar solo si: el estado es COMPLETED y no tiene reseña
    val canReview = booking.status == BookingStatus.COMPLETED && !booking.hasReview

    // Log para debug
    android.util.Log.d("BookingCard", "Booking ${booking.id}: status=${booking.status}, hasReview=${booking.hasReview}, canReview=$canReview")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(booking.serviceTitle, fontWeight = FontWeight.Bold)
                    Text(
                        "${booking.date} ${booking.time}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Box(
                    modifier = Modifier
                        .background(
                            when (displayStatus) {
                                BookingStatus.PENDING -> Color(0xFFFFF3CD)
                                BookingStatus.CONFIRMED -> Color(0xFFD1ECF1)
                                BookingStatus.IN_PROGRESS -> Color(0xFFCCE5FF)
                                BookingStatus.COMPLETED -> Color(0xFFD4EDDA)
                                BookingStatus.CANCELLED -> Color(0xFFF8D7DA)
                            },
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        when (displayStatus) {
                            BookingStatus.PENDING -> "Pendiente"
                            BookingStatus.CONFIRMED -> "Confirmado"
                            BookingStatus.IN_PROGRESS -> "En Progreso"
                            BookingStatus.COMPLETED -> "Completado"
                            BookingStatus.CANCELLED -> "Cancelado"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = when (displayStatus) {
                            BookingStatus.PENDING -> Color(0xFF856404)
                            BookingStatus.CONFIRMED -> Color(0xFF0C5460)
                            BookingStatus.IN_PROGRESS -> Color(0xFF004085)
                            BookingStatus.COMPLETED -> Color(0xFF155724)
                            BookingStatus.CANCELLED -> Color(0xFF721C24)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AttachMoney, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Text("Monto ${FormatUtils.formatPrice(booking.total)}", fontWeight = FontWeight.Bold)
                }

                // Mostrar botón de calificar solo si se puede calificar
                if (canReview) {
                    TextButton(onClick = onReview) {
                        Text("Calificar")
                    }
                }
            }
        }
    }
}
