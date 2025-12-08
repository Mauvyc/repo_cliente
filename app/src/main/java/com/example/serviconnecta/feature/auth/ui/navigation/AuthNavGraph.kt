package com.example.serviconnecta.feature.auth.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.serviconnecta.feature.auth.ui.identity.IdentityCaptureScreen
import com.example.serviconnecta.feature.auth.ui.identity.IdentityInstructionsScreen
import com.example.serviconnecta.feature.auth.ui.identity.IdentityIntroScreen
import com.example.serviconnecta.feature.auth.ui.identity.IdentityOverviewScreen
import com.example.serviconnecta.feature.auth.ui.identity.IdentitySelectDocumentScreen
import com.example.serviconnecta.feature.auth.ui.identity.IdentityStartScreen
import com.example.serviconnecta.feature.auth.ui.identity.IdentitySuccessScreen
import com.example.serviconnecta.feature.auth.ui.identity.IdentityVerificationViewModel
import com.example.serviconnecta.feature.auth.ui.login.LoginScreen
import com.example.serviconnecta.feature.auth.ui.login.LoginViewModel
import com.example.serviconnecta.feature.auth.ui.register.AccountTypeScreen
import com.example.serviconnecta.feature.auth.ui.register.RegisterDataInfoScreen
import com.example.serviconnecta.feature.auth.ui.register.RegisterPhoneScreen
import com.example.serviconnecta.feature.auth.ui.register.RegistrationViewModel
import com.example.serviconnecta.feature.auth.ui.register.VerifyPhoneCodeScreen

@Composable
fun AuthNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registrationViewModel: RegistrationViewModel,
    identityVerificationViewModel: IdentityVerificationViewModel,
    onLoginSuccess: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AuthDestination.Login.route
    ) {
        composable(AuthDestination.Login.route) {

            // Observamos el estado del login
            val loginUiState by loginViewModel.uiState.collectAsState()

            // Cuando haya loginSuccess y accountType, avisamos a MainNavGraph
            LaunchedEffect(loginUiState.loginSuccess, loginUiState.accountType) {
                val accountType = loginUiState.accountType
                if (loginUiState.loginSuccess && accountType != null) {
                    onLoginSuccess(accountType)
                }
            }

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                },
                onRegisterClick = {
                    navController.navigate(AuthDestination.RegisterAccountType.route)
                }
            )
        }

        composable(AuthDestination.RegisterAccountType.route) {

            LaunchedEffect(Unit) {
                registrationViewModel.resetState()
            }

            val uiState by registrationViewModel.uiState.collectAsState()

            AccountTypeScreen(
                uiState = uiState,
                onAccountTypeSelected = { registrationViewModel.setAccountType(it) },
                onConfirmClick = { navController.navigate(AuthDestination.RegisterPhone.route) },
                onCancelClick = { navController.popBackStack() }
            )
        }


        composable(AuthDestination.RegisterDataInfo.route) {
            RegisterDataInfoScreen(
                onConfirmClick = {
                    navController.navigate(AuthDestination.RegisterPhone.route)
                },
                onCancelClick = { navController.popBackStack() }
            )
        }

        composable(AuthDestination.RegisterPhone.route) {
            val uiState by registrationViewModel.uiState.collectAsState()

            RegisterPhoneScreen(
                uiState = uiState,
                onPhoneChange = registrationViewModel::setPhoneNumber,
                onPasswordChange = registrationViewModel::setPassword,
                onConfirmPasswordChange = registrationViewModel::setConfirmPassword,
                onTermsChange = registrationViewModel::setAcceptedTerms,
                onPrivacyChange = registrationViewModel::setAcceptedPrivacy,

                // 1) Registro + info personal + request SMS
                onConfirmClick = { fullName, email, gender, birthDate, onRegisterSuccess ->
                    registrationViewModel.updateRegistrationForm(
                        fullName = fullName,
                        email = email,
                        gender = gender,
                        birthDate = birthDate
                    )
                    registrationViewModel.registerAndRequestCode {
                        onRegisterSuccess()       // -> muestra el dialog
                    }
                },

                // 2) Confirmar código desde el dialog
                onVerificationConfirm = { onSuccess ->
                    registrationViewModel.confirmVerification {
                        onSuccess()

                        navController.navigate(AuthDestination.IdentityOverview.route) {
                            popUpTo(AuthDestination.RegisterPhone.route) {
                                inclusive = true
                            }
                        }
                    }
                },

                onLoginClick = {
                    navController.popBackStack(AuthDestination.Login.route, inclusive = false)
                },

                onCodeChange = registrationViewModel::setVerificationCode
            )
        }


        composable(AuthDestination.RegisterVerifyCode.route) {
            val uiState by registrationViewModel.uiState.collectAsState()
            VerifyPhoneCodeScreen(
                uiState = uiState,
                onCodeChange = registrationViewModel::setVerificationCode,
                onVerifyClick = {
                    registrationViewModel.confirmVerification {
                        navController.navigate(AuthDestination.RegisterIdentityIntro.route)
                    }
                },
                onCancelClick = { navController.popBackStack() }
            )
        }

        composable(AuthDestination.RegisterIdentityIntro.route) {
            IdentityIntroScreen(
                onStartClick = {
                    navController.navigate(AuthDestination.IdentityStart.route)
                }
            )
        }

        composable(AuthDestination.IdentityStart.route) {
            IdentityStartScreen(
                onContinueClick = {
                    // siguiente pantalla del flujo de identidad
                    navController.navigate(AuthDestination.IdentitySelectDocument.route)
                }
            )
        }

        composable(AuthDestination.IdentitySelectDocument.route) {
            val uiState by identityVerificationViewModel.uiState.collectAsState()
            IdentitySelectDocumentScreen(
                uiState = uiState,
                onLoadOptions = { identityVerificationViewModel.loadOptions() },
                onSelectDocumentType = identityVerificationViewModel::selectDocumentType,
                onContinueClick = {
                    navController.navigate(AuthDestination.IdentityInstructions.route)

                }
            )
        }

        composable(AuthDestination.IdentityInstructions.route) {
            IdentityInstructionsScreen(
                onTakePhotoClick = {
                    navController.navigate(AuthDestination.IdentityCapture.route)
                }
            )
        }

        composable(AuthDestination.IdentityOverview.route) {
            val uiState by identityVerificationViewModel.uiState.collectAsState()

            IdentityOverviewScreen(
                uiState = uiState,
                onLoadOptions = { identityVerificationViewModel.loadOptions() },
                onSelectDocumentType = identityVerificationViewModel::selectDocumentType,
                onContinueClick = {
                    // Ir directo a la pantalla de cámara
                    navController.navigate(AuthDestination.IdentityCapture.route)
                }
            )
        }

        composable(AuthDestination.IdentityCapture.route) {
            val uiState by identityVerificationViewModel.uiState.collectAsState()

            IdentityCaptureScreen(
                uiState = uiState,
                onPhotoCaptured = { imageBase64 ->
                    identityVerificationViewModel.submitDocument(
                        imageBase64 = imageBase64
                    ) {
                        navController.navigate(AuthDestination.IdentitySuccess.route)
                    }
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable(AuthDestination.IdentitySuccess.route) {
            IdentitySuccessScreen(
                onFinishClick = {
                    registrationViewModel.resetState()
                    // Por ejemplo: volver al login y limpiar el stack
                    navController.navigate(AuthDestination.Login.route) {
                        popUpTo(AuthDestination.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
