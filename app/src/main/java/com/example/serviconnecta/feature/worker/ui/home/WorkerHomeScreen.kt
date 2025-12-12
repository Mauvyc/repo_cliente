package com.example.serviconnecta.feature.worker.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serviconnecta.core.ui.components.RatingBar
import com.example.serviconnecta.core.utils.FormatUtils
import com.example.serviconnecta.feature.worker.data.WorkerRepository
import com.example.serviconnecta.ui.theme.ServiBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerHomeScreen(
    userPreferences: com.example.serviconnecta.core.datastore.UserPreferences,
    onNavigateToServices: () -> Unit,
    onNavigateToRequests: () -> Unit,
    onNavigateToProfile: () -> Unit,
    workerRepository: WorkerRepository
) {
    val workerHomeViewModel: WorkerHomeViewModel = viewModel(
        factory = WorkerHomeViewModelFactory(repository = workerRepository)
    )

    val uiState by workerHomeViewModel.uiState.collectAsState()
    val userName by userPreferences.fullNameFlow.collectAsState(initial = "Usuario")

    LaunchedEffect(Unit) {
        workerHomeViewModel.loadData()
    }

    Scaffold(
        topBar = {
            if (!uiState.isLoading) {
                TopAppBar(
                    title = {
                        Column {
                            Text("Hola,", style = MaterialTheme.typography.bodySmall)
                            Text(
                                userName ?: "Usuario",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToServices,
                    icon = { Icon(Icons.Default.List, null) },
                    label = { Text("Servicios") }
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
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    if (uiState.nextReservation != null && uiState.nextReservation!!.exists) {
                        val nextReservation = uiState.nextReservation
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = ServiBlue)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column {
                                        Text(
                                            nextReservation?.clientName ?: "",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            nextReservation?.serviceTitle ?: "",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White.copy(alpha = 0.9f)
                                        )
                                    }
                                    IconButton(onClick = onNavigateToRequests) {
                                        Icon(Icons.Default.ArrowForward, null, tint = Color.White)
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Row {
                                    Icon(Icons.Default.DateRange, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(nextReservation?.scheduledDate ?: "", color = Color.White, style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Icon(Icons.Default.AccessTime, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "${nextReservation?.timeRange?.start} - ${nextReservation?.timeRange?.end}",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            "No tienes solicitudes próximas.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Mis Servicios", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        TextButton(onClick = onNavigateToServices) {
                            Text("Añadir servicio")
                        }
                    }
                }

                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Publica qué haces y desde qué zona trabajas para que los clientes te encuentren.")
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onNavigateToServices,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Publicar servicio")
                            }
                        }
                    }
                }

                item {
                    Text("Tu Calificación", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Promedio de puntuación basado en las reseñas de los clientes.")
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RatingBar(rating = uiState.ratingSummary?.averageRating ?: 0.0, starSize = 24)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    FormatUtils.formatRating(uiState.ratingSummary?.averageRating ?: 0.0),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
