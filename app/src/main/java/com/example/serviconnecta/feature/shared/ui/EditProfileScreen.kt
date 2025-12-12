package com.example.serviconnecta.feature.shared.ui


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    userPreferences: com.example.serviconnecta.core.datastore.UserPreferences,
    viewModel: EditProfileViewModel,
    onNavigateBack: () -> Unit
) {
    val currentName by userPreferences.fullNameFlow.collectAsState(initial = "")
    val currentEmail by userPreferences.emailFlow.collectAsState(initial = "")
    val currentPhone by userPreferences.phoneNumberFlow.collectAsState(initial = "")

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Cargar valores actuales
    LaunchedEffect(currentName, currentEmail, currentPhone) {
        name = currentName ?: ""
        email = currentEmail ?: ""
        phone = currentPhone ?: ""
    }

    val context = LocalContext.current
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val updateSuccess = viewModel.updateSuccess

    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            Toast
                .makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT)
                .show()
            viewModel.consumeSuccess()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                IconButton(
                    onClick = { },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(Icons.Default.Edit, "Editar foto", tint = MaterialTheme.colorScheme.primary)
                }
            }

            Text(currentName ?: "Usuario", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Celular") },
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.saveProfile(name, email, phone)
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp
                    )
                }
                Text("Actualizar")
            }
        }
    }
}
