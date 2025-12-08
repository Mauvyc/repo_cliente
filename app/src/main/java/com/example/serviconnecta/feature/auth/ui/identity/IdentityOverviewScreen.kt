package com.example.serviconnecta.feature.auth.ui.identity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serviconnecta.feature.auth.domain.model.IdentityDocumentType
import com.example.serviconnecta.ui.theme.ServiBlue

@Composable
fun IdentityOverviewScreen(
    uiState: IdentityUiState,
    onLoadOptions: () -> Unit,
    onSelectDocumentType: (String) -> Unit,
    onContinueClick: () -> Unit
) {
    // Cargamos opciones del backend una sola vez
    LaunchedEffect(Unit) {
        onLoadOptions()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 380.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                // ---------- Título y descripción ----------
                Text(
                    text = "Empecemos",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Para garantizar la seguridad de tu cuenta y protegerla contra el fraude, te pedimos que completes nuestro proceso de verificación de identidad.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ---------- Solo “Foto documento de identidad” ----------
                IdentityInfoRow(
                    title = "Foto Documento de Identidad",
                    description = "Se admiten documentos de identidad, pasaportes y licencias de conducir."
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ---------- País / región emisor ----------
                Text(
                    text = "País/región emisor del documento",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                CountrySelector(
                    currentCountry = uiState.country   // por ahora siempre "PE"
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ---------- Tipo de documento ----------
                Text(
                    text = "¿Qué método prefieres utilizar?",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Si vienen tipos desde el backend, los mostramos; si no, usamos 3 por defecto
                val documentTypes: List<IdentityDocumentType> =
                    if (uiState.documentTypes.isNotEmpty()) {
                        uiState.documentTypes
                    } else {
                        listOf(
                            IdentityDocumentType("NATIONAL_ID", "Documento de identidad"),
                            IdentityDocumentType("PASSPORT", "Pasaporte"),
                            IdentityDocumentType("DRIVER_LICENSE", "Licencia de conducir")
                        )
                    }

                documentTypes.forEach { type ->
                    IdentityDocumentOptionCard(
                        label = type.label,
                        code = type.code,
                        isSelected = uiState.selectedDocumentTypeCode == type.code,
                        onClick = { onSelectDocumentType(type.code) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ---------- Texto legal ----------
                Text(
                    text = "Al hacer clic en «Continuar», confirmas que has leído y aceptas la declaración de autenticación de identidad del usuario.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(14.dp))

                // ---------- Botón Continuar ----------
                Button(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = uiState.selectedDocumentTypeCode != null && !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ServiBlue,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Continuar")
                }

                uiState.errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun IdentityInfoRow(
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(ServiBlue.copy(alpha = 0.08f), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Description, // ícono genérico de documento
                contentDescription = null,
                tint = ServiBlue
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun CountrySelector(
    currentCountry: String
) {
    // Por ahora solo Perú, pero dejamos la UI como selector
    OutlinedCard(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .clickable(enabled = false) {}   // desactivado por ahora
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // “Bandera” simple
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .background(Color.Red, shape = RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PE",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Perú",
                modifier = Modifier.weight(1f),
                fontSize = 14.sp
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun IdentityDocumentOptionCard(
    label: String,
    code: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor =
        if (isSelected) ServiBlue else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    val bgColor =
        if (isSelected) ServiBlue.copy(alpha = 0.06f) else MaterialTheme.colorScheme.surface

    OutlinedCard(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.outlinedCardColors(
            containerColor = bgColor
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        ServiBlue.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                val icon = when (code) {
                    "NATIONAL_ID" -> Icons.Default.Badge
                    "PASSPORT" -> Icons.Default.CreditCard
                    "DRIVER_LICENSE" -> Icons.Default.CreditCard
                    else -> Icons.Default.Description
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ServiBlue
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = label,
                modifier = Modifier.weight(1f),
                fontSize = 14.sp
            )

            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
        }
    }
}
