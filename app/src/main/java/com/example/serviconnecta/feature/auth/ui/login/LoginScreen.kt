package com.example.serviconnecta.feature.auth.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serviconnecta.R
import com.example.serviconnecta.ui.theme.ServiBlue

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) onLoginSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 360.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo centrado (más grande)
            Image(
                painter = painterResource(id = R.drawable.ic_serviconecta_logo),
                contentDescription = "Logo ServiConecta",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(96.dp)
                    .width(96.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Bienvenido",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Inicie sesión utilizando su número de teléfono móvil",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            PhoneAndPasswordFields(
                phone = uiState.phoneNumber,
                onPhoneChange = viewModel::onPhoneNumberChanged,
                password = uiState.password,
                onPasswordChange = viewModel::onPasswordChanged,
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage,
                onLoginClick = viewModel::onLoginClicked,
                onRegisterClick = onRegisterClick
            )
        }
    }
}

@Composable
private fun PhoneAndPasswordFields(
    phone: String,
    onPhoneChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    // Teléfono: solo dígitos + teclado numérico
    OutlinedTextField(
        value = phone,
        onValueChange = { newValue ->
            val digitsOnly = newValue.filter { it.isDigit() }
            onPhoneChange(digitsOnly)
        },
        label = { Text("Ingrese su número de teléfono") },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ServiBlue,
            unfocusedBorderColor = ServiBlue.copy(alpha = 0.5f),
            focusedLabelColor = ServiBlue,
            cursorColor = ServiBlue
        )
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Contraseña") },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ServiBlue,
            unfocusedBorderColor = ServiBlue.copy(alpha = 0.5f),
            focusedLabelColor = ServiBlue,
            cursorColor = ServiBlue
        )
    )

    Spacer(modifier = Modifier.height(20.dp))

    Button(
        onClick = onLoginClick,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ServiBlue,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        if (isLoading) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Cargando...")
            }
        } else {
            Text(text = "Ingresar")
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    errorMessage?.let { error ->
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    Row {
        Text(text = "¿No tienes una cuenta? ")
        Text(
            text = "Regístrate",
            fontWeight = FontWeight.Bold,
            color = ServiBlue,
            modifier = Modifier.clickable { onRegisterClick() }
        )
    }
}
