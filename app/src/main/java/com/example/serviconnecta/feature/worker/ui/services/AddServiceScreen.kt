package com.example.serviconnecta.feature.worker.ui.services

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddServiceViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir servicio") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Titulo", style = MaterialTheme.typography.labelMedium)
            OutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::updateTitle,
                placeholder = { Text("Coloque aquí su título...") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Descripción", style = MaterialTheme.typography.labelMedium)
            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::updateDescription,
                placeholder = { Text("Coloque la descripción aquí...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Text("Servicio", style = MaterialTheme.typography.labelMedium)
            var expandedCategory by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = it }
            ) {
                OutlinedTextField(
                    value = uiState.category,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedCategory) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false }
                ) {
                    listOf("Servicio de electricidad", "Servicio de gasfitería", "Servicio de albañilería").forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                viewModel.updateCategory(cat)
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            Text("Precio", style = MaterialTheme.typography.labelMedium)
            OutlinedTextField(
                value = uiState.price,
                onValueChange = viewModel::updatePrice,
                placeholder = { Text("Coloque aquí su precio en (S/.)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Archivo de medio", style = MaterialTheme.typography.labelMedium)
            Text("Añade la portada de tu servicio", style = MaterialTheme.typography.bodySmall)

            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.PhotoCamera, null, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Busca tu imagen de portada")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("OR", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        TextButton(onClick = { }) {
                            Text("Buscar foto")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = viewModel::createService,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Crear servicio")
                }
            }

            uiState.errorMessage?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                LaunchedEffect(error) {
                    kotlinx.coroutines.delay(3000)
                    viewModel.clearError()
                }
            }
        }
    }
}
