package com.example.serviconnecta.feature.auth.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serviconnecta.ui.theme.ServiBlue

@Composable
fun AccountTypeScreen(
    uiState: RegistrationUiState,
    onAccountTypeSelected: (AccountType) -> Unit,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // 👇 Todo centrado en la pantalla
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HeaderSection()

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AccountTypeCard(
                        title = "Conecta Pro",
                        subtitle = "Ofrece servicios, recibe pagos y gana visibilidad.",
                        isSelected = uiState.accountType == AccountType.CONECTA_PRO,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.BusinessCenter,
                                contentDescription = null,
                                tint = ServiBlue
                            )
                        },
                        onClick = { onAccountTypeSelected(AccountType.CONECTA_PRO) },
                        modifier = Modifier.weight(1f)
                    )

                    AccountTypeCard(
                        title = "Cliente",
                        subtitle = "Encuentra profesionales y reserva servicios.",
                        isSelected = uiState.accountType == AccountType.CLIENTE,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = ServiBlue
                            )
                        },
                        onClick = { onAccountTypeSelected(AccountType.CLIENTE) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                InfoHint()

                Spacer(modifier = Modifier.height(16.dp))

                // ---------- Botón Continuar con spinner ----------
                Button(
                    onClick = onConfirmClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = uiState.accountType != null && !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ServiBlue,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    if (uiState.isLoading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Cargando...")
                        }
                    } else {
                        Text(text = "Continuar")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onCancelClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = ServiBlue
                    )
                ) {
                    Text(text = "Cancelar")
                }
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(ServiBlue.copy(alpha = 0.08f))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Crear cuenta",
                color = ServiBlue,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "¿Cómo usarás ServiConecta?",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Elige el tipo de cuenta que mejor se adapta a ti.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun AccountTypeCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor =
        if (isSelected) ServiBlue else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    val backgroundColor =
        if (isSelected) ServiBlue.copy(alpha = 0.06f) else MaterialTheme.colorScheme.surface
    val titleColor =
        if (isSelected) ServiBlue else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(ServiBlue.copy(alpha = 0.1f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = titleColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
        }
    }
}

@Composable
private fun InfoHint() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ServiBlue.copy(alpha = 0.06f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(ServiBlue.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "i",
                color = ServiBlue,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Siempre podrás cambiar o crear otro tipo de cuenta más adelante.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}
