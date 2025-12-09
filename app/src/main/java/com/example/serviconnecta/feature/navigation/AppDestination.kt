package com.example.serviconnecta.feature.navigation

sealed class AppDestination(val route: String) {

    // Worker destinations
    data object WorkerHome : AppDestination("worker/home")
    data object WorkerServices : AppDestination("worker/services")
    data object WorkerServiceDetail : AppDestination("worker/service/{serviceId}") {
        fun createRoute(serviceId: String) = "worker/service/$serviceId"
    }
    data object WorkerEditService : AppDestination("worker/edit-service/{serviceId}") {
        fun createRoute(serviceId: String) = "worker/edit-service/$serviceId"
    }
    data object WorkerAddService : AppDestination("worker/add-service")
    data object WorkerRequests : AppDestination("worker/requests")
    data object WorkerReservations : AppDestination("worker/reservations")
    data object WorkerSchedule : AppDestination("worker/schedule")
    data object WorkerProfile : AppDestination("worker/profile")
    data object WorkerEditProfile : AppDestination("worker/edit-profile")
    data object WorkerChangePassword : AppDestination("worker/change-password")
    data object WorkerMyReviews : AppDestination("worker/reviews")
    data object WorkerMyAreas : AppDestination("worker/areas")
    data object WorkerPrivacyPolicy : AppDestination("worker/privacy-policy")
    data object WorkerTerms : AppDestination("worker/terms")

    // Client destinations
    data object ClientHome : AppDestination("client/home")
    data object ClientSearch : AppDestination("client/search")
    data object ClientServicesList : AppDestination("client/services/list")
    data object ClientServicesByCategory : AppDestination("client/services/category/{category}") {
        fun createRoute(category: String) = "client/services/category/$category"
    }
    data object ClientServiceDetail : AppDestination("client/service/{serviceId}") {
        fun createRoute(serviceId: String) = "client/service/$serviceId"
    }
    data object ClientProviderDetail : AppDestination("client/provider/{providerId}") {
        fun createRoute(providerId: String) = "client/provider/$providerId"
    }
    data object ClientBookingSummary : AppDestination("client/booking/summary/{serviceId}") {
        fun createRoute(serviceId: String) = "client/booking/summary/$serviceId"
    }
    data object ClientSelectDateTime : AppDestination("client/booking/datetime/{serviceId}") {
        fun createRoute(serviceId: String) = "client/booking/datetime/$serviceId"
    }
    data object ClientPaymentMethod : AppDestination("client/booking/payment/{serviceId}/{date}/{timeRange}/{locationId}") {
        fun createRoute(serviceId: String, date: String, timeRange: String, locationId: String) =
            "client/booking/payment/$serviceId/$date/$timeRange/$locationId"
    }
    data object ClientLocations : AppDestination("client/locations")
    data object ClientAddLocation : AppDestination("client/add-location")
    data object ClientSelectLocation : AppDestination("client/select-location")

    data object ClientReservations : AppDestination("client/reservations")
    data object ClientWriteReview : AppDestination("client/review/{bookingId}") {
        fun createRoute(bookingId: String) = "client/review/$bookingId"
    }
    data object ClientProfile : AppDestination("client/profile")
    data object ClientEditProfile : AppDestination("client/edit-profile")
    data object ClientChangePassword : AppDestination("client/change-password")
    data object ClientPrivacyPolicy : AppDestination("client/privacy-policy")
    data object ClientTerms : AppDestination("client/terms")
}
