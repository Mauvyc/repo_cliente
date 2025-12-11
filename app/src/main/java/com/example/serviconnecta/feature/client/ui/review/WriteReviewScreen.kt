package com.example.serviconnecta.feature.client.ui.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteReviewScreen(
    bookingId: String,  // ID de la solicitud de servicio (request_id)
    onNavigateBack: () -> Unit,
    viewModel: WriteReviewViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(bookingId) {
        viewModel.loadBooking(bookingId)
    }

    LaunchedEffect(uiState.submitSuccess) {
        if (uiState.submitSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calificar Servicio") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.booking == null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.error ?: "No se pudo cargar la información",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadBooking(bookingId) }) {
                            Text("Reintentar")
                        }
                    }
                }
                else -> {
                    ReviewContent(
                        booking = uiState.booking!!,
                        serviceRating = uiState.serviceRating,
                        providerRating = uiState.providerRating,
                        selectedHighlights = uiState.selectedHighlights,
                        comment = uiState.comment,
                        isSubmitting = uiState.isSubmitting,
                        error = uiState.error,
                        onServiceRatingChange = { viewModel.updateServiceRating(it) },
                        onProviderRatingChange = { viewModel.updateProviderRating(it) },
                        onHighlightToggle = { viewModel.toggleHighlight(it) },
                        onCommentChange = { viewModel.updateComment(it) },
                        onSubmit = { viewModel.submitReview(bookingId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewContent(
    booking: com.example.serviconnecta.feature.client.domain.model.Booking,
    serviceRating: Int,
    providerRating: Int,
    selectedHighlights: Set<String>,
    comment: String,
    isSubmitting: Boolean,
    error: String?,
    onServiceRatingChange: (Int) -> Unit,
    onProviderRatingChange: (Int) -> Unit,
    onHighlightToggle: (String) -> Unit,
    onCommentChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Información del servicio
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar del proveedor
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = booking.providerName.first().uppercase(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = booking.providerName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = booking.serviceTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${booking.date} • ${booking.time}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Calificación del Servicio
        Text(
            text = "Califica el servicio",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            for (i in 1..5) {
                Icon(
                    imageVector = if (i <= serviceRating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = "Estrella servicio $i",
                    tint = if (i <= serviceRating) Color(0xFFFFC107) else Color.Gray,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onServiceRatingChange(i) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Calificación del Proveedor
        Text(
            text = "Califica al proveedor",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            for (i in 1..5) {
                Icon(
                    imageVector = if (i <= providerRating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = "Estrella proveedor $i",
                    tint = if (i <= providerRating) Color(0xFFFFC107) else Color.Gray,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onProviderRatingChange(i) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Aspectos destacados
        Text(
            text = "Aspectos destacados (opcional)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        val highlightOptions = listOf(
            "Calidad del servicio",
            "Cumplimiento del horario",
            "Buena comunicación",
            "Precio justo",
            "Profesionalismo",
            "Limpieza"
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            highlightOptions.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { highlight ->
                        val isSelected = selectedHighlights.contains(highlight)
                        FilterChip(
                            selected = isSelected,
                            onClick = { onHighlightToggle(highlight) },
                            label = { Text(highlight) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Agregar espacio si solo hay un item en la fila
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de comentario
        Text(
            text = "Cuéntanos más (opcional)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = comment,
            onValueChange = onCommentChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            placeholder = { Text("Escribe tu opinión sobre el servicio...") },
            maxLines = 6,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mensaje de error
        if (error != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botón de enviar
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isSubmitting && serviceRating > 0 && providerRating > 0,
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = "Enviar Reseña",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
