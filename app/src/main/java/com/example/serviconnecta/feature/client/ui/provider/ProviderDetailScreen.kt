package com.example.serviconnecta.feature.client.ui.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serviconnecta.core.ui.components.RatingBar
import com.example.serviconnecta.core.utils.FormatUtils
import com.example.serviconnecta.feature.client.domain.model.Provider
import com.example.serviconnecta.feature.client.domain.model.ServiceItem
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderDetailScreen(
    providerId: String,
    onNavigateBack: () -> Unit,
    onNavigateToServiceDetail: (String) -> Unit,
    viewModel: ProviderDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(providerId) {
        viewModel.loadProviderDetail(providerId)
    }

    val provider = uiState.providerDetail?.provider
    val services = uiState.providerDetail?.services ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del Trabajador") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
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
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.List, null) },
                    label = { Text("Reservas") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
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
                        Text(
                            uiState.error ?: "Error desconocido",
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadProviderDetail(providerId) }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            provider == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Error, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No se pudo cargar la información del trabajador", color = Color.Gray)
                    }
                }
            }
            else -> {
                ProviderDetailContent(
                    provider = provider,
                    services = services,
                    onServiceClick = onNavigateToServiceDetail,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
private fun ProviderDetailContent(
    provider: Provider,
    services: List<ServiceItem>,
    onServiceClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Perfil del trabajador
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    ) {
                        if (provider.photo != null) {
                            AsyncImage(
                                model = provider.photo,
                                contentDescription = "Foto de ${provider.name}",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(60.dp),
                                tint = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        provider.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        provider.specialty,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RatingBar(rating = provider.rating, starSize = 20)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            FormatUtils.formatRating(provider.rating),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Información adicional
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Acerca de",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val yearsText = provider.yearsExperience ?: 5

                    Text(
                        "Profesional con más de $yearsText años de experiencia en ${provider.specialty.lowercase()}. " +
                                "Comprometido con la calidad y la satisfacción del cliente. Trabajo garantizado.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    InfoRow(Icons.Default.CheckCircle, "Verificado")
                    Spacer(modifier = Modifier.height(8.dp))

                    // 👇 Usamos reviewCount real
                    InfoRow(Icons.Default.Star, "${provider.reviewCount} Reseñas")

                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow(Icons.Default.Build, "${services.size} Servicios activos")

                }
            }
        }

        // Servicios ofrecidos
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Servicios Ofrecidos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${services.size} servicios",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        if (services.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Info, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No hay servicios disponibles", color = Color.Gray)
                        }
                    }
                }
            }
        } else {
            items(services) { service ->
                ServiceCard(service, onClick = { onServiceClick(service.id) })
            }
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ServiceCard(service: ServiceItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.Image, null, modifier = Modifier.align(Alignment.Center))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingBar(rating = service.rating, starSize = 14)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("(${FormatUtils.formatRating(service.rating)})", style = MaterialTheme.typography.bodySmall)
                }
                Text(service.title, fontWeight = FontWeight.Bold)
                Text(service.category, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    FormatUtils.formatPrice(service.price),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(Icons.Default.ArrowForward, null, modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}
