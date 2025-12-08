package com.example.serviconnecta.feature.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.serviconnecta.feature.auth.ui.identity.IdentityVerificationViewModel
import com.example.serviconnecta.feature.auth.ui.login.LoginViewModel
import com.example.serviconnecta.feature.auth.ui.navigation.AuthNavGraph
import com.example.serviconnecta.feature.auth.ui.register.RegistrationViewModel
import com.example.serviconnecta.feature.client.ui.home.ClientHomeViewModel
import com.example.serviconnecta.feature.client.ui.locations.ClientLocationsViewModel
import com.example.serviconnecta.feature.client.ui.provider.ProviderDetailViewModel
import com.example.serviconnecta.feature.client.ui.reservations.ReservationsViewModel
import com.example.serviconnecta.feature.client.ui.review.WriteReviewViewModel
import com.example.serviconnecta.feature.client.ui.search.SearchViewModel
import com.example.serviconnecta.feature.client.ui.services.AllServicesViewModel
import com.example.serviconnecta.feature.client.ui.services.BookingViewModel
import com.example.serviconnecta.feature.client.ui.services.ServicesByCategoryViewModel
import com.example.serviconnecta.feature.shared.ui.ChangePasswordViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import com.example.serviconnecta.feature.shared.ui.EditProfileViewModel
import com.example.serviconnecta.feature.worker.data.WorkerRepository

@Composable
fun MainNavGraph(
    loginViewModel: LoginViewModel,
    registrationViewModel: RegistrationViewModel,
    identityVerificationViewModel: IdentityVerificationViewModel,
    accountTypeFlow: Flow<String?>,
    userPreferences: com.example.serviconnecta.core.datastore.UserPreferences,
    editProfileViewModel: EditProfileViewModel,
    changePasswordViewModel: ChangePasswordViewModel,
    servicesByCategoryViewModel: ServicesByCategoryViewModel,
    clientHomeViewModel: ClientHomeViewModel,
    bookingViewModel: BookingViewModel,
    reservationsViewModel: ReservationsViewModel,
    clientLocationsViewModel: ClientLocationsViewModel,
    searchViewModel: SearchViewModel,
    writeReviewViewModel: WriteReviewViewModel,
    providerDetailViewModel: ProviderDetailViewModel,
    allServicesViewModel: AllServicesViewModel,
    workerRepository: WorkerRepository,
    onClearSession: suspend () -> Unit
) {
    val rootNavController = rememberNavController()
    var currentAccountType by remember { mutableStateOf<String?>(null) }

    // Solo para auto-login al iniciar la app
    LaunchedEffect(Unit) {
        val savedAccountType = accountTypeFlow.firstOrNull()

        if (savedAccountType != null) {
            when (savedAccountType) {
                "CONECTA_PRO" -> {
                    rootNavController.navigate("worker") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
                "CLIENTE" -> {
                    rootNavController.navigate("client") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            }
        }
    }

    NavHost(
        navController = rootNavController,
        startDestination = "auth"
    ) {
        composable("auth") {
            val authNavController = rememberNavController()

            LaunchedEffect(Unit) {
                loginViewModel.resetState()   // crea esta función en tu LoginViewModel
            }

            AuthNavGraph(
                navController = authNavController,
                loginViewModel = loginViewModel,
                registrationViewModel = registrationViewModel,
                identityVerificationViewModel = identityVerificationViewModel,
                onLoginSuccess = { accountType ->
                    currentAccountType = accountType
                    when (accountType) {
                        "CONECTA_PRO" -> rootNavController.navigate("worker") {
                            popUpTo("auth") { inclusive = true }
                        }
                        "CLIENTE" -> rootNavController.navigate("client") {
                            popUpTo("auth") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("worker") {
            val workerNavController = rememberNavController()
            WorkerNavGraph(
                navController = workerNavController,
                userPreferences = userPreferences,
                viewModel = editProfileViewModel,
                changePasswordViewModel = changePasswordViewModel,
                workerRepository = workerRepository,
                onLogout = {
                    kotlinx.coroutines.runBlocking {
                        onClearSession()
                    }
                    currentAccountType = null
                    loginViewModel.resetState()
                    rootNavController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("client") {
            val clientNavController = rememberNavController()
            ClientNavGraph(
                navController = clientNavController,
                userPreferences = userPreferences,
                editProfileViewModel = editProfileViewModel,
                changePasswordViewModel = changePasswordViewModel,
                servicesByCategoryViewModel = servicesByCategoryViewModel,
                clientHomeViewModel = clientHomeViewModel,
                bookingViewModel = bookingViewModel,
                reservationsViewModel = reservationsViewModel,
                clientLocationsViewModel = clientLocationsViewModel,
                searchViewModel = searchViewModel,
                writeReviewViewModel = writeReviewViewModel,
                providerDetailViewModel = providerDetailViewModel,
                allServicesViewModel = allServicesViewModel,
                onLogout = {
                    kotlinx.coroutines.runBlocking {
                        onClearSession()
                    }
                    clientHomeViewModel.reset()
                    currentAccountType = null
                    rootNavController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
