package com.example.serviconnecta.feature.client.ui.services

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.serviconnecta.feature.client.domain.model.PaymentMethod
import com.example.serviconnecta.feature.client.domain.model.PaymentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(
    serviceId: String,
    date: String,
    timeRange: String,
    locationId: String,
    onNavigateBack: () -> Unit,
    onPaymentSuccess: () -> Unit,
    viewModel: BookingViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddCardDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.paymentMethods, uiState.selectedPaymentMethodId) {
        android.util.Log.d("PaymentScreen", "UI paymentMethods=${uiState.paymentMethods.size}")
        uiState.paymentMethods.forEach {
            android.util.Log.d(
                "PaymentScreen",
                "UI -> id=${it.id}, type=${it.type}, label=${it.label}, last4=${it.last4}, isDefault=${it.isDefault}"
            )
        }
        android.util.Log.d("PaymentScreen", "selectedPaymentMethodId=${uiState.selectedPaymentMethodId}")
    }


    // 🔹 Cada vez que entro a esta pantalla, pido los métodos de pago
    LaunchedEffect(Unit) {
        viewModel.loadPaymentMethods()
    }

    // 🔹 Cuando el pago fue exitoso, navego y marco como manejado
//    LaunchedEffect(uiState.showPaymentSuccess) {
//        if (uiState.showPaymentSuccess) {
//            onPaymentSuccess()
//            viewModel.onPaymentSuccessHandled()
//        }
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Método de Pago") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading && uiState.paymentMethods.isEmpty() -> {
                    // ⏳ Cargando inicial
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.error != null && uiState.paymentMethods.isEmpty() -> {
                    // ❌ Error y no tengo nada en memoria
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            uiState.error ?: "Error al cargar métodos de pago",
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.loadPaymentMethods() }) {
                            Text("Reintentar")
                        }
                    }
                }

                uiState.paymentMethods.isEmpty() -> {
                    // 🚫 No hay métodos de pago
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.CreditCard,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "No tienes métodos de pago guardados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Agrega una tarjeta o selecciona un método disponible.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { showAddCardDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Agregar Nueva Tarjeta")
                        }
                    }
                }

                else -> {
                    // ✅ Tengo métodos (y quizá también error, pero ya hay algo cacheado)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // Lista de métodos de pago
                        uiState.paymentMethods.forEach { method ->
                            PaymentMethodCard(
                                paymentMethod = method,
                                isSelected = uiState.selectedPaymentMethodId == method.id,
                                onClick = { viewModel.selectPaymentMethod(method.id) }
                            )
                        }

                        Button(
                            onClick = { showAddCardDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Add, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Agregar Nueva Tarjeta")
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = { viewModel.confirmBooking() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.selectedPaymentMethodId != null && !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White
                                )
                            } else {
                                Text("Pagar")
                            }
                        }
                    }
                }
            }
        }
    }

    // 🔹 Diálogo para agregar tarjeta
    if (showAddCardDialog) {
        AddCardDialog(
            onDismiss = { showAddCardDialog = false },
            onConfirm = { number, holder, month, year, cvv ->
                viewModel.addNewCard(
                    cardNumber = number,
                    cardHolderName = holder,
                    expMonth = month,
                    expYear = year,
                    cvv = cvv
                )
                showAddCardDialog = false
            }
        )
    }

    // 🔹 Dialogito de éxito de pago (si lo tienes)
    if (uiState.showPaymentSuccess) {
        PaymentSuccessDialog(
            onClose = {
                viewModel.onPaymentSuccessHandled()
                onPaymentSuccess()
            }
        )
    }

}

@Composable
private fun PaymentMethodCard(
    paymentMethod: PaymentMethod,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    when (paymentMethod.type) {
                        PaymentType.CASH -> Icons.Default.Money
                        PaymentType.PAYPAL -> Icons.Default.Payment
                        PaymentType.GOOGLE_PAY -> Icons.Default.Payment
                        PaymentType.APPLE_PAY -> Icons.Default.Smartphone // placeholder
                        PaymentType.CARD -> Icons.Default.CreditCard
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        when (paymentMethod.type) {
                            PaymentType.CASH -> "Efectivo"
                            PaymentType.PAYPAL -> "PayPal"
                            PaymentType.GOOGLE_PAY -> "Google Pay"
                            PaymentType.APPLE_PAY -> "Apple Pay"
                            PaymentType.CARD -> "**** **** **** ${paymentMethod.last4}"
                        },
                        fontWeight = FontWeight.Bold
                    )
                    if (paymentMethod.type == PaymentType.CARD) {
                        Text(
                            paymentMethod.cardholderName ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
        }
    }
}

@Composable
fun PaymentSuccessDialog(
    onClose: () -> Unit
) {
    Dialog(onDismissRequest = { }) {
        Card {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    null,
                    modifier = Modifier.size(80.dp),
                    tint = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Pago Exitoso",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "¡Gracias por su preferencia!",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Su pago ha sido procesado exitosamente.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = onClose) {
                    Text("Ver mis reservas")
                }
            }
        }
    }
}

@Composable
fun AddCardDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, Int, String) -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var holderName by remember { mutableStateOf("") }
    var expMonth by remember { mutableStateOf("") }
    var expYear by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val canConfirm = cardNumber.length >= 12 &&
            holderName.isNotBlank() &&
            expMonth.toIntOrNull() != null &&
            expYear.toIntOrNull() != null &&
            cvv.length in 3..4

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar nueva tarjeta") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { cardNumber = it },
                    label = { Text("Número de tarjeta") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = holderName,
                    onValueChange = { holderName = it },
                    label = { Text("Nombre del titular") },
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = expMonth,
                        onValueChange = { expMonth = it.filter { c -> c.isDigit() }.take(2) },
                        label = { Text("Mes") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = expYear,
                        onValueChange = { expYear = it.filter { c -> c.isDigit() }.take(4) },
                        label = { Text("Año") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it.filter { c -> c.isDigit() }.take(4) },
                    label = { Text("CVV") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val month = expMonth.toIntOrNull() ?: return@Button
                    val year = expYear.toIntOrNull() ?: return@Button
                    onConfirm(cardNumber, holderName, month, year, cvv)
                },
                enabled = canConfirm
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
