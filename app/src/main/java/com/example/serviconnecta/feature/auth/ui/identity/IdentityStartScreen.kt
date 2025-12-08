package com.example.serviconnecta.feature.auth.ui.identity

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serviconnecta.ui.theme.ServiBlue

@Composable
fun IdentityStartScreen(
    onContinueClick: () -> Unit
) {
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
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                // Título principal
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

                IdentityItem(
                    title = "Foto Documento de Identidad",
                    description = "Se admiten documentos de identidad, pasaportes y permisos de conducir."
                )

                Spacer(modifier = Modifier.height(12.dp))

                IdentityItem(
                    title = "Foto Certificado Único Laboral",
                    description = "Se aceptan certificados expedidos por el estado o por tu empleador."
                )

                Spacer(modifier = Modifier.height(12.dp))

                IdentityItem(
                    title = "Prueba de domicilio",
                    description = "Documentos que acrediten tu dirección, como facturas de servicios, etc."
                )

                Spacer(modifier = Modifier.height(12.dp))

                IdentityItem(
                    title = "Reconocimiento facial",
                    description = "Confirmaremos que tu foto coincide con el documento de identidad."
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Al hacer clic en el botón «Aceptar y continuar», confirmas que has leído y aceptas la Declaración de información de autenticación de identidad del usuario.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ServiBlue,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Aceptar y continuar")
                }
            }
        }
    }
}

@Composable
private fun IdentityItem(
    title: String,
    description: String
) {
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