package com.example.serviconnecta.feature.worker.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerProfileScreen(
    userPreferences: com.example.serviconnecta.core.datastore.UserPreferences,
    onNavigateBack: () -> Unit,
    onNavigateToServices: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    onNavigateToReviews: () -> Unit,
    onNavigateToAreas: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onLogout: () -> Unit
) {
    val userName by userPreferences.fullNameFlow.collectAsState(initial = "Usuario")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil") }
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
                    onClick = onNavigateToServices,
                    icon = { Icon(Icons.Default.List, null) },
                    label = { Text("Servicios") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(userName ?: "Usuario", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                ProfileMenuItem(
                    icon = Icons.Default.Edit,
                    title = "Editar Perfil",
                    onClick = onNavigateToEditProfile
                )
            }

            item {
                ProfileMenuItem(
                    icon = Icons.Default.Lock,
                    title = "Cambiar Contraseña",
                    onClick = onNavigateToChangePassword
                )
            }

            item {
                ProfileMenuItem(
                    icon = Icons.Default.Star,
                    title = "Mis Reseñas",
                    onClick = onNavigateToReviews
                )
            }

            item {
                ProfileMenuItem(
                    icon = Icons.Default.LocationOn,
                    title = "Mis Áreas",
                    onClick = onNavigateToAreas
                )
            }

            item {
                ProfileMenuItem(
                    icon = Icons.Default.Security,
                    title = "Politica de Privacidad",
                    onClick = onNavigateToPrivacyPolicy
                )
            }

            item {
                ProfileMenuItem(
                    icon = Icons.Default.Description,
                    title = "Términos y Condiciones",
                    onClick = onNavigateToTerms
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                TextButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.ExitToApp, null, tint = Color.Red)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar sesión", color = Color.Red)
                }
            }
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(title)
            }
            Icon(Icons.Default.ArrowForward, null)
        }
    }
}
