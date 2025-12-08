package com.example.serviconnecta.feature.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.serviconnecta.feature.client.ui.services.PaymentMethodScreen
import com.example.serviconnecta.feature.client.ui.home.ClientHomeScreen
import com.example.serviconnecta.feature.client.ui.home.ClientHomeViewModel
import com.example.serviconnecta.feature.client.ui.locations.ClientLocationsScreen
import com.example.serviconnecta.feature.client.ui.locations.ClientLocationsViewModel
import com.example.serviconnecta.feature.client.ui.profile.ClientProfileScreen
import com.example.serviconnecta.feature.client.ui.reservations.MyReservationsScreen
import com.example.serviconnecta.feature.client.ui.services.ServiceDetailScreen
import com.example.serviconnecta.feature.client.ui.services.ServicesByCategoryScreen
import com.example.serviconnecta.feature.client.ui.services.AllServicesScreen
import com.example.serviconnecta.feature.client.ui.review.WriteReviewScreen
import com.example.serviconnecta.feature.client.ui.provider.ProviderDetailScreen
import com.example.serviconnecta.feature.client.ui.reservations.ReservationsViewModel
import com.example.serviconnecta.feature.client.ui.search.SearchScreen
import com.example.serviconnecta.feature.client.ui.search.SearchViewModel
import com.example.serviconnecta.feature.client.ui.services.AllServicesViewModel
import com.example.serviconnecta.feature.client.ui.services.BookingSummaryScreen
import com.example.serviconnecta.feature.client.ui.services.BookingViewModel
import com.example.serviconnecta.feature.client.ui.services.SelectDateTimeScreen
import com.example.serviconnecta.feature.shared.ui.ChangePasswordScreen
import com.example.serviconnecta.feature.shared.ui.ChangePasswordViewModel
import com.example.serviconnecta.feature.shared.ui.EditProfileScreen
import com.example.serviconnecta.feature.shared.ui.EditProfileViewModel
import com.example.serviconnecta.feature.shared.ui.PrivacyPolicyScreen
import com.example.serviconnecta.feature.shared.ui.TermsScreen
import com.example.serviconnecta.feature.client.ui.services.ServicesByCategoryViewModel

@Composable
fun ClientNavGraph(
    navController: NavHostController,
    userPreferences: com.example.serviconnecta.core.datastore.UserPreferences,
    editProfileViewModel: EditProfileViewModel,
    changePasswordViewModel: ChangePasswordViewModel,
    servicesByCategoryViewModel: ServicesByCategoryViewModel,
    clientHomeViewModel: ClientHomeViewModel,
    bookingViewModel: BookingViewModel,
    reservationsViewModel: ReservationsViewModel,
    clientLocationsViewModel: ClientLocationsViewModel,
    searchViewModel: SearchViewModel,
    writeReviewViewModel: com.example.serviconnecta.feature.client.ui.review.WriteReviewViewModel,
    providerDetailViewModel: com.example.serviconnecta.feature.client.ui.provider.ProviderDetailViewModel,
    allServicesViewModel: AllServicesViewModel,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.ClientHome.route
    ) {
        composable(AppDestination.ClientHome.route) {
            ClientHomeScreen(
                onNavigateToSearch = {
                    navController.navigate(AppDestination.ClientSearch.route)
                },
                onNavigateToCategory = { categoryId ->
                    navController.navigate(
                        AppDestination.ClientServicesByCategory.createRoute(
                            categoryId
                        )
                    )
                },
                onNavigateToServicesList = {
                    navController.navigate(AppDestination.ClientServicesList.route)
                },
                onNavigateToServiceDetail = { serviceId ->
                    navController.navigate(AppDestination.ClientServiceDetail.createRoute(serviceId))
                },
                onNavigateToReservations = {
                    navController.navigate(AppDestination.ClientReservations.route)
                },
                onNavigateToProfile = {
                    navController.navigate(AppDestination.ClientProfile.route)
                },
                onNavigateToProvider = { providerId ->
                    navController.navigate(AppDestination.ClientProviderDetail.createRoute(providerId))
                },
                onNavigateToLocations = {
                    navController.navigate(AppDestination.ClientLocations.route)
                },
                viewModel = clientHomeViewModel
            )
        }

        composable(AppDestination.ClientSearch.route) {
            SearchScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToServiceDetail = { serviceId ->
                    navController.navigate(AppDestination.ClientServiceDetail.createRoute(serviceId))
                },
                onNavigateToCategory = { category ->
                    navController.navigate(AppDestination.ClientServicesByCategory.createRoute(category))
                },
                viewModel = searchViewModel
            )
        }

        composable(AppDestination.ClientServicesList.route) {
            AllServicesScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToServiceDetail = { serviceId ->
                    navController.navigate(AppDestination.ClientServiceDetail.createRoute(serviceId))
                },
                viewModel = allServicesViewModel
            )
        }

        composable(
            route = AppDestination.ClientServicesByCategory.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("category") ?: return@composable
            ServicesByCategoryScreen(
                category = categoryId,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToServiceDetail = { serviceId ->
                    navController.navigate(AppDestination.ClientServiceDetail.createRoute(serviceId))
                },
                viewModel = servicesByCategoryViewModel
            )
        }


        composable(
            route = AppDestination.ClientProviderDetail.route,
            arguments = listOf(navArgument("providerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val providerId = backStackEntry.arguments?.getString("providerId") ?: return@composable
            ProviderDetailScreen(
                providerId = providerId,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToServiceDetail = { serviceId ->
                    navController.navigate(AppDestination.ClientServiceDetail.createRoute(serviceId))
                },
                viewModel = providerDetailViewModel
            )
        }

        composable(
            route = AppDestination.ClientServiceDetail.route,
            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: return@composable
            ServiceDetailScreen(
                serviceId = serviceId,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToBooking = { sid ->
                    navController.navigate(AppDestination.ClientBookingSummary.createRoute(sid))
                },
                viewModel = bookingViewModel
            )
        }

        composable(
            route = AppDestination.ClientBookingSummary.route,
            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: return@composable

            BookingSummaryScreen(
                serviceId = serviceId,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToSelectLocation = {
                    navController.navigate(AppDestination.ClientSelectLocation.route)
                },
                onNavigateToDateTime = { sid ->
                    navController.navigate(
                        AppDestination.ClientSelectDateTime.createRoute(sid)
                    )
                },
                viewModel = bookingViewModel
            )
        }

        composable(
            route = AppDestination.ClientSelectDateTime.route,
            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: return@composable

            SelectDateTimeScreen(
                serviceId = serviceId,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToPayment = { sid, date, timeRange, locationId ->
                    navController.navigate(
                        AppDestination.ClientPaymentMethod.createRoute(
                            sid,
                            date,
                            timeRange,
                            locationId
                        )
                    )
                },
                viewModel = bookingViewModel
            )
        }

        composable(
            route = AppDestination.ClientPaymentMethod.route,
            arguments = listOf(
                navArgument("serviceId") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("timeRange") { type = NavType.StringType },
                navArgument("locationId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: return@composable
            val date = backStackEntry.arguments?.getString("date") ?: return@composable
            val timeRange = backStackEntry.arguments?.getString("timeRange") ?: return@composable
            val locationId = backStackEntry.arguments?.getString("locationId") ?: return@composable

            PaymentMethodScreen(
                serviceId = serviceId,
                date = date,
                timeRange = timeRange,
                locationId = locationId,
                onNavigateBack = { navController.navigateUp() },
                onPaymentSuccess = {
                    navController.navigate(AppDestination.ClientReservations.route) {
                        popUpTo(AppDestination.ClientHome.route)
                    }
                },
                viewModel = bookingViewModel
            )
        }

        composable(AppDestination.ClientReservations.route) {
            MyReservationsScreen(
                onNavigateToHome = {
                    navController.navigate(AppDestination.ClientHome.route) {
                        popUpTo(AppDestination.ClientHome.route) { inclusive = true }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(AppDestination.ClientProfile.route)
                },
                onNavigateToReview = { bookingId ->
                    navController.navigate(AppDestination.ClientWriteReview.createRoute(bookingId))
                },
                viewModel = reservationsViewModel
            )
        }

        composable(
            route = AppDestination.ClientWriteReview.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: return@composable
            WriteReviewScreen(
                bookingId = bookingId,
                onNavigateBack = { navController.navigateUp() },
                viewModel = writeReviewViewModel
            )
        }

        composable(AppDestination.ClientProfile.route) {
            ClientProfileScreen(
                userPreferences = userPreferences,
                onNavigateToHome = {
                    navController.navigate(AppDestination.ClientHome.route) {
                        popUpTo(AppDestination.ClientHome.route) { inclusive = true }
                    }
                },
                onNavigateToReservations = {
                    navController.navigate(AppDestination.ClientReservations.route)
                },
                onNavigateToEditProfile = {
                    navController.navigate(AppDestination.ClientEditProfile.route)
                },
                onNavigateToChangePassword = {
                    navController.navigate(AppDestination.ClientChangePassword.route)
                },
                onNavigateToMyReservations = {
                    navController.navigate(AppDestination.ClientReservations.route)
                },
                onNavigateToLocations = {
                    navController.navigate(AppDestination.ClientLocations.route)
                },
                onNavigateToPrivacyPolicy = {
                    navController.navigate(AppDestination.ClientPrivacyPolicy.route)
                },
                onNavigateToTerms = {
                    navController.navigate(AppDestination.ClientTerms.route)
                },
                onLogout = onLogout
            )
        }

        composable(AppDestination.ClientEditProfile.route) {
            EditProfileScreen(
                userPreferences = userPreferences,
                viewModel = editProfileViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(AppDestination.ClientChangePassword.route) {
            ChangePasswordScreen(
                viewModel = changePasswordViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(AppDestination.ClientSelectLocation.route) {
            ClientLocationsScreen(
                onNavigateBack = { navController.navigateUp() },
                onLocationSelected = { location ->
                    bookingViewModel.selectLocation(location.id)
                    bookingViewModel.loadLocations()
                    navController.navigateUp()
                },
                viewModel = clientLocationsViewModel
            )
        }

        composable(AppDestination.ClientLocations.route) {
            ClientLocationsScreen(
                onNavigateBack = { navController.navigateUp() },
                onLocationSelected = { location ->
                    // Actualizar la ubicación seleccionada en el home
                    clientHomeViewModel.updateSelectedLocation(location)
                    navController.navigateUp()
                },
                viewModel = clientLocationsViewModel
            )
        }

        composable(AppDestination.ClientPrivacyPolicy.route) {
            PrivacyPolicyScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(AppDestination.ClientTerms.route) {
            TermsScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
