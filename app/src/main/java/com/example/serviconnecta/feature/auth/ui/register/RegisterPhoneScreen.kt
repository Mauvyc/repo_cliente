package com.example.serviconnecta.feature.auth.ui.register

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serviconnecta.ui.theme.ServiBlue
import java.util.Calendar

@Composable
fun RegisterPhoneScreen(
    uiState: RegistrationUiState,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTermsChange: (Boolean) -> Unit,
    onPrivacyChange: (Boolean) -> Unit,
    onConfirmClick: (
        fullName: String,
        email: String,
        gender: GenderType?,
        birthDate: String,
        onRegisterSuccess: () -> Unit
    ) -> Unit,
    onVerificationConfirm: (onSuccess: () -> Unit) -> Unit,
    onLoginClick: () -> Unit,
    onCodeChange: (String) -> Unit
) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf(uiState.fullName) }
    var email by remember { mutableStateOf(uiState.email) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf(uiState.gender) }
    var birthDate by remember { mutableStateOf(uiState.birthDate) }
    var showVerificationDialog by remember { mutableStateOf(false) }

    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 380.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Crea una nueva cuenta",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Configure su usuario y contraseña\ncámbielo en cualquier momento.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // NOMBRE
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Nombre completo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // TELÉFONO solo dígitos
            OutlinedTextField(
                value = uiState.phoneNumber,
                onValueChange = { newValue ->
                    val digitsOnly = newValue.filter { it.isDigit() }
                    onPhoneChange(digitsOnly)
                },
                label = { Text("Número telefónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = outlinedColors()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // GÉNERO (simple con chips)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = gender == GenderType.MALE,
                    onClick = { gender = GenderType.MALE },
                    label = { Text("Masculino") }
                )
                FilterChip(
                    selected = gender == GenderType.FEMALE,
                    onClick = { gender = GenderType.FEMALE },
                    label = { Text("Femenino") }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // FECHA NACIMIENTO con DatePickerDialog
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Configuramos un DatePicker con edad mínima 18 años
                        val defaultCal = Calendar.getInstance().apply {
                            add(Calendar.YEAR, -25)
                        }

                        val year = defaultCal.get(Calendar.YEAR)
                        val month = defaultCal.get(Calendar.MONTH)
                        val day = defaultCal.get(Calendar.DAY_OF_MONTH)

                        val dialog = DatePickerDialog(
                            context,
                            { _, y, m, d ->
                                // Formato YYYY-MM-DD
                                birthDate = "%04d-%02d-%02d".format(y, m + 1, d)
                            },
                            year,
                            month,
                            day
                        )

                        // Máximo: hoy - 18 años
                        val maxCal = Calendar.getInstance().apply {
                            add(Calendar.YEAR, -18)
                        }
                        dialog.datePicker.maxDate = maxCal.timeInMillis

                        // Opcional: mínimo hace 100 años
                        val minCal = Calendar.getInstance().apply {
                            add(Calendar.YEAR, -100)
                        }
                        dialog.datePicker.minDate = minCal.timeInMillis

                        dialog.show()
                    }
            ) {
                OutlinedTextField(
                    value = birthDate,
                    onValueChange = { /* readOnly */ },
                    label = { Text("Fecha de nacimiento") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false, // sigue sin permitir escribir
                    colors = OutlinedTextFieldDefaults.colors(
                        // activos (por consistencia)
                        focusedBorderColor = ServiBlue,
                        unfocusedBorderColor = ServiBlue.copy(alpha = 0.5f),
                        focusedLabelColor = ServiBlue,
                        cursorColor = ServiBlue,
                        // 👇 override de disabled para que NO se vea gris
                        disabledBorderColor = ServiBlue.copy(alpha = 0.5f),
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        disabledTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // CONTRASEÑA
            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors(),
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Default.VisibilityOff
                            else
                                Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // CONFIRMAR CONTRASEÑA
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = { Text("Vuelve a colocar contraseña") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors(),
                visualTransformation = if (confirmPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible)
                                Icons.Default.VisibilityOff
                            else
                                Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // TÉRMINOS Y PRIVACIDAD
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = uiState.acceptedTerms,
                    onCheckedChange = onTermsChange
                )
                Text(
                    text = "He leído y acepto los ",
                    fontSize = 12.sp
                )
                Text(
                    text = "términos y condiciones",
                    fontSize = 12.sp,
                    color = ServiBlue,
                    modifier = Modifier.clickable { showTermsDialog = true }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = uiState.acceptedPrivacy,
                    onCheckedChange = onPrivacyChange
                )
                Text(
                    text = "He leído y acepto la ",
                    fontSize = 12.sp
                )
                Text(
                    text = "política de privacidad",
                    fontSize = 12.sp,
                    color = ServiBlue,
                    modifier = Modifier.clickable { showPrivacyDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Button(
                onClick = {
                    onConfirmClick(
                        fullName,
                        email,
                        gender,
                        birthDate
                    ) {
                        showVerificationDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ServiBlue,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = if (uiState.isLoading) "Registrando..." else "Registrarse")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "¿Ya tienes una cuenta? ")
                Text(
                    text = "Ingresar",
                    color = ServiBlue,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Dialog código SMS
    if (showVerificationDialog) {
        PhoneVerificationDialog(
            uiState = uiState,
            onCodeChange = onCodeChange,
            onConfirmClick = {
                onVerificationConfirm {
                    showVerificationDialog = false
                }
            },
            onDismiss = {
                showVerificationDialog = false
            }
        )
    }

    // Dialog Términos
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = { Text("Términos y condiciones") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 0.dp, max = 260.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Estos términos regulan el uso de ServiConecta. " +
                                "Al crear una cuenta declaras que la información proporcionada " +
                                "es real y que utilizarás el servicio únicamente para fines legales. " +
                                "Podremos suspender tu acceso ante actividad sospechosa, fraude " +
                                "o incumplimiento de nuestras políticas.\n\n" +
                                "ServiConecta no es responsable directo de la relación comercial " +
                                "entre clientes y proveedores, pero puede intervenir para " +
                                "investigar reclamos y mejorar la experiencia de la plataforma.",
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // Dialog Privacidad
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Política de privacidad") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 0.dp, max = 260.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "En ServiConecta protegemos tus datos personales. " +
                                "La información que nos proporcionas (nombre, correo, " +
                                "teléfono y documentos de identidad) se utiliza para " +
                                "gestionar tu cuenta, verificar tu identidad y mejorar " +
                                "la seguridad de la plataforma.\n\n" +
                                "No compartimos información con terceros sin tu consentimiento, " +
                                "salvo cuando sea requerido por ley o para prevenir " +
                                "actividades fraudulentas. Puedes solicitar la actualización " +
                                "o eliminación de tus datos según la normativa vigente.",
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
private fun outlinedColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = ServiBlue,
        unfocusedBorderColor = ServiBlue.copy(alpha = 0.5f),
        focusedLabelColor = ServiBlue,
        cursorColor = ServiBlue
    )
}
