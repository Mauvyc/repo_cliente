package com.example.serviconnecta.feature.client.ui.services

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.serviconnecta.feature.client.ui.services.BookingViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SelectDateTimeScreen(
    serviceId: String,
    onNavigateBack: () -> Unit,
    onNavigateToPayment: (String, String, String, String) -> Unit,
    viewModel: BookingViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // fechas próximas (ej: 14 días)
    val today = remember { LocalDate.now() }
    val availableDates = remember {
        (0 until 14).map { today.plusDays(it.toLong()) }
    }
    var selectedDate by remember { mutableStateOf(availableDates.first()) }

    // slots de horario fijos (puedes adaptarlos)
    val timeSlots = listOf("08:00", "11:00", "15:00", "18:00")
    var selectedTime by remember { mutableStateOf(timeSlots.first()) }

    var notes by remember { mutableStateOf(uiState.notes) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val displayFormatter = DateTimeFormatter.ofPattern("dd MMM")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seleccionar espacio") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 4.dp) {
                Button(
                    onClick = {
                        val dateString = selectedDate.format(dateFormatter)
                        // rango de 1h simple
                        val start = selectedTime
                        val end = when (selectedTime) {
                            "08:00" -> "09:00"
                            "11:00" -> "12:00"
                            "15:00" -> "16:00"
                            "18:00" -> "19:00"
                            else -> selectedTime
                        }

                        viewModel.updateSchedule(
                            date = dateString,
                            start = start,
                            end = end,
                            notes = notes
                        )

                        val locationId = uiState.selectedLocationId
                            ?: uiState.locations.firstOrNull()?.id
                            ?: ""

                        // `time` param lo mando como "HH:mm-HH:mm"
                        onNavigateToPayment(serviceId, dateString, "$start-$end", locationId)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Ir a pagar")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Seleccionar Fecha",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Fechas (fila de chips)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                availableDates.forEach { date ->
                    FilterChip(
                        selected = date == selectedDate,
                        onClick = { selectedDate = date },
                        label = {
                            Text(date.format(displayFormatter))
                        }
                    )
                }
            }

            Text(
                "Seleccionar Horario",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                timeSlots.forEach { slot ->
                    FilterChip(
                        selected = slot == selectedTime,
                        onClick = { selectedTime = slot },
                        label = { Text(slot) }
                    )
                }
            }

            Text(
                "Comentario para el profesional",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                placeholder = { Text("Ej: Por favor traer escalera alta…") }
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}