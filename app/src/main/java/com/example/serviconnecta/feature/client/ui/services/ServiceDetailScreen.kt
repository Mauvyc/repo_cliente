package com.example.serviconnecta.feature.client.ui.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.serviconnecta.feature.client.ui.services.BookingViewModel
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    serviceId: String,
    onNavigateBack: () -> Unit,
    onNavigateToBooking: (String) -> Unit,
    viewModel: BookingViewModel
) {
    LaunchedEffect(serviceId) {
        viewModel.loadServiceDetail(serviceId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val service = uiState.service

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Servicio") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        },
        bottomBar = {
            if (service != null) {
                BottomAppBar {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            FormatUtils.formatPrice(service.price),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Button(onClick = { onNavigateToBooking(serviceId) }) {
                            Text("Solicitar")
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (service != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.LightGray)
                ) {
                    if (service.imageUrl != null) {
                        AsyncImage(
                            model = service.imageUrl,
                            contentDescription = service.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.Image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(service.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(FormatUtils.formatPrice(service.price), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Close, "Cerrar")
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RatingBar(rating = service.rating)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("(${FormatUtils.formatRating(service.rating)})")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Descripcion", fontWeight = FontWeight.Bold)
                    Text(service.description, color = Color.Gray)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Acerca del Proveedor de Servicio", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        ) {
                            if (service.provider.photo != null) {
                                AsyncImage(
                                    model = service.provider.photo,
                                    contentDescription = "Foto de ${service.provider.name}",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(service.provider.name, fontWeight = FontWeight.Bold)
                            Text(service.provider.specialty, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Comentarios", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (service.comments.isEmpty()) {
                        Text(
                            "Este servicio aún no tiene comentarios.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            service.comments.forEach { comment ->
                                ServiceCommentItem(
                                    authorName = comment.authorName,
                                    rating = comment.rating,
                                    comment = comment.comment
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ServiceCommentItem(
    authorName: String,
    rating: Double,
    comment: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(authorName, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        RatingBar(rating = rating, starSize = 14)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "(${FormatUtils.formatRating(rating)})",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                comment,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

