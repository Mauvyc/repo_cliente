package com.example.serviconnecta.feature.auth.ui.identity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serviconnecta.feature.auth.domain.model.IdentityDocumentType
import com.example.serviconnecta.ui.theme.ServiBlue

@Composable
fun IdentitySelectDocumentScreen(
    uiState: IdentityUiState,
    onLoadOptions: () -> Unit,
    onSelectDocumentType: (String) -> Unit,
    onContinueClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        onLoadOptions()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Selecciona el tipo de identificación", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "País emisor: ${uiState.country}")

                Spacer(modifier = Modifier.height(16.dp))

                uiState.documentTypes.forEach { type: IdentityDocumentType ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectDocumentType(type.code) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.selectedDocumentTypeCode == type.code,
                            onClick = { onSelectDocumentType(type.code) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = type.label)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = uiState.selectedDocumentTypeCode != null && !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ServiBlue
                    )
                ) {
                    Text(text = "Continuar")
                }

                uiState.errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it)
                }
            }
        }
    }
}