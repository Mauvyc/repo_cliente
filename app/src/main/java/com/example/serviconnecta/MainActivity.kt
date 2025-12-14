package com.example.serviconnecta

import com.example.serviconnecta.feature.shared.usecase.UpdateUserProfileUseCase
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.serviconnecta.core.datastore.AuthPreferences
import com.example.serviconnecta.core.datastore.LocationPreferences
import com.example.serviconnecta.core.datastore.UserPreferences
import com.example.serviconnecta.core.datastore.ReviewedServicesPreferences
import com.example.serviconnecta.core.datastore.authDataStore
import com.example.serviconnecta.core.datastore.locationDataStore
import com.example.serviconnecta.core.datastore.userDataStore
import com.example.serviconnecta.core.datastore.reviewedServicesDataStore
import com.example.serviconnecta.core.network.AuthInterceptor
import com.example.serviconnecta.core.network.NetworkMonitor
import com.example.serviconnecta.core.network.NoInternetDialog
import com.example.serviconnecta.core.network.RetrofitProvider
import com.example.serviconnecta.feature.auth.data.AuthRepositoryImpl
import com.example.serviconnecta.feature.auth.data.IdentityVerificationRepository
import com.example.serviconnecta.feature.auth.data.UserProfileRepository
import com.example.serviconnecta.feature.auth.data.remote.AuthApiService
import com.example.serviconnecta.feature.auth.data.remote.IdentityVerificationApiService
import com.example.serviconnecta.feature.auth.data.remote.UserApiService
import com.example.serviconnecta.feature.auth.domain.usecase.*
import com.example.serviconnecta.feature.auth.ui.identity.IdentityVerificationViewModel
import com.example.serviconnecta.feature.auth.ui.login.LoginViewModel
import com.example.serviconnecta.feature.auth.ui.register.RegistrationViewModel
import com.example.serviconnecta.feature.navigation.MainNavGraph
import com.example.serviconnecta.ui.theme.ServiconnectaTheme
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import com.example.serviconnecta.core.session.SessionManager
import com.example.serviconnecta.feature.client.data.ClientServicesRepository
import com.example.serviconnecta.feature.client.data.remote.ClientApiService
import com.example.serviconnecta.feature.client.domain.usecase.GetClientHomeUseCase
import com.example.serviconnecta.feature.client.domain.usecase.GetClientReservationsUseCase
import com.example.serviconnecta.feature.client.domain.usecase.GetProviderDetailUseCase
import com.example.serviconnecta.feature.client.domain.usecase.GetServiceDetailUseCase
import com.example.serviconnecta.feature.client.domain.usecase.GetServicesByCategoryUseCase
import com.example.serviconnecta.feature.client.domain.usecase.SearchServicesUseCase
import com.example.serviconnecta.feature.client.domain.usecase.GetBookingByIdUseCase
import com.example.serviconnecta.feature.client.domain.usecase.SubmitReviewUseCase
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
import com.example.serviconnecta.feature.shared.ui.EditProfileViewModel
import com.example.serviconnecta.feature.shared.usecase.ChangePasswordUseCase
import com.example.serviconnecta.feature.worker.data.WorkerRepositoryImpl
import com.example.serviconnecta.feature.worker.data.remote.WorkerApiService
import com.example.serviconnecta.feature.worker.domain.usecase.GetMyReviewsUseCase
import com.example.serviconnecta.feature.worker.ui.home.WorkerHomeViewModel
import com.example.serviconnecta.feature.worker.ui.home.WorkerHomeViewModelFactory
import com.example.serviconnecta.feature.worker.ui.reviews.MyReviewsViewModel
import com.example.serviconnecta.feature.worker.ui.reviews.MyReviewsViewModelFactory


class MainActivity : ComponentActivity() {

    private lateinit var networkMonitor: NetworkMonitor
    private val authPreferences by lazy {
        AuthPreferences(applicationContext.authDataStore)
    }

    private val userPreferences by lazy {
        UserPreferences(applicationContext.authDataStore)
    }

    private val sessionManager by lazy {
        SessionManager(authPreferences, userPreferences)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DataStore
        val authPreferences = AuthPreferences(authDataStore)
        val userPreferences = UserPreferences(userDataStore)
        val locationPreferences = LocationPreferences(locationDataStore)
        val reviewedServicesPreferences = ReviewedServicesPreferences(
            applicationContext.reviewedServicesDataStore
        )

        // --- Monitor de red global ---
        networkMonitor = NetworkMonitor(applicationContext)

        // Interceptor que lee el accessToken de DataStore
        val authInterceptor = AuthInterceptor {
            // Bloqueamos brevemente para leer el token actual
            runBlocking {
                authPreferences.accessTokenFlow.firstOrNull()
            }
        }

        // Retrofit con auth (para endpoints que requieren Authorization)
        val retrofitAuthed = RetrofitProvider.createAuthRetrofit(authInterceptor)

        // APIs
        val authApi = retrofitAuthed.create(AuthApiService::class.java)
        val userApi = retrofitAuthed.create(UserApiService::class.java)
        val identityApi = retrofitAuthed.create(IdentityVerificationApiService::class.java)
        val clientApi = retrofitAuthed.create(ClientApiService::class.java)
        val workerApiService = retrofitAuthed.create(WorkerApiService::class.java)

        // Repos
        val authRepository = AuthRepositoryImpl(authApi, authPreferences, userPreferences)
        val userProfileRepository = UserProfileRepository(userApi)
        val identityVerificationRepository = IdentityVerificationRepository(identityApi)
        val clientServicesRepository = ClientServicesRepository(clientApi, reviewedServicesPreferences)
        val workerRepository = WorkerRepositoryImpl(workerApiService)

        // Use cases
        val loginUseCase = LoginUseCase(authRepository)
        val registerUseCase = RegisterUseCase(authRepository)
        val requestPhoneVerificationUseCase = RequestPhoneVerificationUseCase(authRepository)
        val confirmPhoneVerificationUseCase = ConfirmPhoneVerificationUseCase(authRepository)
        val updatePersonalInfoUseCase = UpdatePersonalInfoUseCase(userProfileRepository)
        val getIdentityOptionsUseCase = GetIdentityOptionsUseCase(identityVerificationRepository)
        val submitIdentityDocumentUseCase = SubmitIdentityDocumentUseCase(identityVerificationRepository)
        val updateUserProfileUseCase = UpdateUserProfileUseCase(userProfileRepository)
        val changePasswordUseCase = ChangePasswordUseCase(authRepository)
        val getServicesByCategoryUseCase = GetServicesByCategoryUseCase(clientServicesRepository)
        val getClientHomeUseCase = GetClientHomeUseCase(clientServicesRepository)
        val getServiceDetailUseCase = GetServiceDetailUseCase(clientServicesRepository)
        val getClientReservationsUseCase = GetClientReservationsUseCase(clientServicesRepository)
        val getBookingByIdUseCase = GetBookingByIdUseCase(clientServicesRepository)
        val searchServicesUseCase = SearchServicesUseCase(clientServicesRepository)
        val submitReviewUseCase = SubmitReviewUseCase(clientServicesRepository)
        val getProviderDetailUseCase = GetProviderDetailUseCase(clientServicesRepository)
        val getMyReviewsUseCase = GetMyReviewsUseCase(workerRepository)


        // ViewModel factories...

        val loginVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return LoginViewModel(loginUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val registrationVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return RegistrationViewModel(
                        registerUseCase = registerUseCase,
                        requestPhoneVerificationUseCase = requestPhoneVerificationUseCase,
                        confirmPhoneVerificationUseCase = confirmPhoneVerificationUseCase,
                        updatePersonalInfoUseCase = updatePersonalInfoUseCase
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val identityVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(IdentityVerificationViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return IdentityVerificationViewModel(
                        getIdentityOptionsUseCase = getIdentityOptionsUseCase,
                        submitIdentityDocumentUseCase = submitIdentityDocumentUseCase
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val loginViewModel =
            ViewModelProvider(this, loginVmFactory)[LoginViewModel::class.java]

        val registrationViewModel =
            ViewModelProvider(this, registrationVmFactory)[RegistrationViewModel::class.java]

        val identityVerificationViewModel =
            ViewModelProvider(this, identityVmFactory)[IdentityVerificationViewModel::class.java]

        val editProfileVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return EditProfileViewModel(
                        updateUserProfileUseCase = updateUserProfileUseCase,
                        userPreferences = userPreferences
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val editProfileViewModel =
            ViewModelProvider(this, editProfileVmFactory)[EditProfileViewModel::class.java]

        val changePasswordVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ChangePasswordViewModel(
                        changePasswordUseCase = changePasswordUseCase
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val changePasswordViewModel =
            ViewModelProvider(this, changePasswordVmFactory)[ChangePasswordViewModel::class.java]

        val servicesByCategoryVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ServicesByCategoryViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ServicesByCategoryViewModel(
                        getServicesByCategoryUseCase = getServicesByCategoryUseCase
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val servicesByCategoryViewModel =
            ViewModelProvider(this, servicesByCategoryVmFactory)[ServicesByCategoryViewModel::class.java]

        // Factory VM
        val clientHomeVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ClientHomeViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ClientHomeViewModel(
                        getClientHomeUseCase = getClientHomeUseCase,
                        locationPreferences = locationPreferences,
                        clientServicesRepository = clientServicesRepository
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        // Instancia
        val clientHomeViewModel =
            ViewModelProvider(this, clientHomeVmFactory)[ClientHomeViewModel::class.java]

        // Factory BookingViewModel
        val bookingVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return BookingViewModel(
                        getServiceDetailUseCase = getServiceDetailUseCase,
                        clientServicesRepository = clientServicesRepository
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        // Instancia
        val bookingViewModel =
            ViewModelProvider(this, bookingVmFactory)[BookingViewModel::class.java]

        val reservationsVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ReservationsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ReservationsViewModel(
                        getClientReservationsUseCase = getClientReservationsUseCase,
                        reviewedServicesPreferences = reviewedServicesPreferences
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val reservationsViewModel =
            ViewModelProvider(this, reservationsVmFactory)[ReservationsViewModel::class.java]

        val locationsVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ClientLocationsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ClientLocationsViewModel(
                        clientServicesRepository = clientServicesRepository
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val clientLocationsViewModel =
            ViewModelProvider(this, locationsVmFactory)[ClientLocationsViewModel::class.java]

        val searchVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SearchViewModel(
                        searchServicesUseCase = searchServicesUseCase
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }

        val searchViewModel =
            ViewModelProvider(this, searchVmFactory)[SearchViewModel::class.java]

        val writeReviewVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(WriteReviewViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return WriteReviewViewModel(
                        getBookingByIdUseCase = getBookingByIdUseCase,
                        submitReviewUseCase = submitReviewUseCase
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }

        val writeReviewViewModel =
            ViewModelProvider(this, writeReviewVmFactory)[WriteReviewViewModel::class.java]

        val providerDetailVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProviderDetailViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ProviderDetailViewModel(
                        getProviderDetailUseCase = getProviderDetailUseCase
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }

        val providerDetailViewModel =
            ViewModelProvider(this, providerDetailVmFactory)[ProviderDetailViewModel::class.java]

        val allServicesVmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AllServicesViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return AllServicesViewModel(
                        clientServicesRepository = clientServicesRepository
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }

        val allServicesViewModel =
            ViewModelProvider(this, allServicesVmFactory)[AllServicesViewModel::class.java]

        val workerHomeViewModel: WorkerHomeViewModel = ViewModelProvider(
            this,
            WorkerHomeViewModelFactory(repository = workerRepository)
        ).get(WorkerHomeViewModel::class.java)

        val myReviewsVmFactory = MyReviewsViewModelFactory(getMyReviewsUseCase)
        val myReviewsViewModel: MyReviewsViewModel = ViewModelProvider(
            this,
            myReviewsVmFactory
        ).get(MyReviewsViewModel::class.java)

        setContent {
            ServiconnectaTheme {
                // Observamos el estado de conexión de red
                val isOnline by networkMonitor.isOnline.collectAsState()

                Box(modifier = Modifier.fillMaxSize()) {
                    MainNavGraph(
                        loginViewModel = loginViewModel,
                        registrationViewModel = registrationViewModel,
                        identityVerificationViewModel = identityVerificationViewModel,
                        accountTypeFlow = authPreferences.accountTypeFlow,
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
                        workerRepository = workerRepository,
                        myReviewsViewModel = myReviewsViewModel,
                        onClearSession = {
                            sessionManager.clearSession()
                        }
                    )

                    // Si NO hay internet, mostramos el diálogo bloqueante
                    if (!isOnline) {
                        NoInternetDialog()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Liberamos el callback de red
        if (this::networkMonitor.isInitialized) {
            networkMonitor.unregister()
        }
    }
}