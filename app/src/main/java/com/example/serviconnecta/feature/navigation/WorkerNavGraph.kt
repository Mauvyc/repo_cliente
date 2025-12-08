package com.example.serviconnecta.feature.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.serviconnecta.core.network.RetrofitProvider
import com.example.serviconnecta.feature.shared.ui.ChangePasswordScreen
import com.example.serviconnecta.feature.shared.ui.ChangePasswordViewModel
import com.example.serviconnecta.feature.shared.ui.EditProfileScreen
import com.example.serviconnecta.feature.shared.ui.EditProfileViewModel
import com.example.serviconnecta.feature.shared.ui.PrivacyPolicyScreen
import com.example.serviconnecta.feature.shared.ui.TermsScreen
import com.example.serviconnecta.feature.worker.data.WorkerRepository
import com.example.serviconnecta.feature.worker.data.WorkerRepositoryImpl
import com.example.serviconnecta.feature.worker.data.remote.WorkerApiService
import com.example.serviconnecta.feature.worker.ui.areas.MyAreasScreen
import com.example.serviconnecta.feature.worker.ui.home.WorkerHomeScreen
import com.example.serviconnecta.feature.worker.ui.profile.WorkerProfileScreen
import com.example.serviconnecta.feature.worker.ui.requests.RequestsScreen
import com.example.serviconnecta.feature.worker.ui.reviews.MyReviewsScreen
import com.example.serviconnecta.feature.worker.ui.schedule.ScheduleScreen
import com.example.serviconnecta.feature.worker.ui.services.AddServiceScreen
import com.example.serviconnecta.feature.worker.ui.services.EditServiceScreen
import com.example.serviconnecta.feature.worker.ui.services.WorkerServicesScreen

@Composable
fun WorkerNavGraph(
    navController: NavHostController,
    userPreferences: com.example.serviconnecta.core.datastore.UserPreferences,
    viewModel: EditProfileViewModel,
    changePasswordViewModel: ChangePasswordViewModel,
    workerRepository: WorkerRepository,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.WorkerHome.route
    ) {
        composable(AppDestination.WorkerHome.route) {
            WorkerHomeScreen(
                userPreferences = userPreferences,
                onNavigateToServices = {
                    navController.navigate(AppDestination.WorkerServices.route)
                },
                onNavigateToRequests = {
                    navController.navigate(AppDestination.WorkerRequests.route)
                },
                onNavigateToSchedule = {
                    navController.navigate(AppDestination.WorkerSchedule.route)
                },
                onNavigateToProfile = {
                    navController.navigate(AppDestination.WorkerProfile.route)
                },
                workerRepository = workerRepository
            )
        }

        composable(AppDestination.WorkerServices.route) {
            WorkerServicesScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToAddService = {
                    navController.navigate(AppDestination.WorkerAddService.route)
                },
                onNavigateToServiceDetail = { serviceId ->
                    navController.navigate(AppDestination.WorkerEditService.createRoute(serviceId))
                },
                onNavigateToRequests = {
                    navController.navigate(AppDestination.WorkerRequests.route)
                },
                onNavigateToProfile = {
                    navController.navigate(AppDestination.WorkerProfile.route)
                }
            )
        }

        composable(AppDestination.WorkerAddService.route) {
            AddServiceScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = AppDestination.WorkerEditService.route,
            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: return@composable
            EditServiceScreen(
                serviceId = serviceId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(AppDestination.WorkerRequests.route) {
            RequestsScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToProfile = {
                    navController.navigate(AppDestination.WorkerProfile.route)
                }
            )
        }

        composable(AppDestination.WorkerSchedule.route) {
            ScheduleScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToProfile = {
                    navController.navigate(AppDestination.WorkerProfile.route)
                }
            )
        }

        composable(AppDestination.WorkerProfile.route) {
            WorkerProfileScreen(
                userPreferences = userPreferences,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToServices = {
                    navController.navigate(AppDestination.WorkerServices.route)
                },
                onNavigateToEditProfile = {
                    navController.navigate(AppDestination.WorkerEditProfile.route)
                },
                onNavigateToChangePassword = {
                    navController.navigate(AppDestination.WorkerChangePassword.route)
                },
                onNavigateToReviews = {
                    navController.navigate(AppDestination.WorkerMyReviews.route)
                },
                onNavigateToAreas = {
                    navController.navigate(AppDestination.WorkerMyAreas.route)
                },
                onNavigateToPrivacyPolicy = {
                    navController.navigate(AppDestination.WorkerPrivacyPolicy.route)
                },
                onNavigateToTerms = {
                    navController.navigate(AppDestination.WorkerTerms.route)
                },
                onLogout = onLogout
            )
        }

        composable(AppDestination.WorkerEditProfile.route) {
            EditProfileScreen(
                userPreferences = userPreferences,
                viewModel = viewModel,        // 👈 ahora SÍ existe
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(AppDestination.WorkerChangePassword.route) {
            ChangePasswordScreen(
                viewModel = changePasswordViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(AppDestination.WorkerMyReviews.route) {
            MyReviewsScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(AppDestination.WorkerMyAreas.route) {
            MyAreasScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(AppDestination.WorkerPrivacyPolicy.route) {
            PrivacyPolicyScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(AppDestination.WorkerTerms.route) {
            TermsScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
