# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Serviconnecta** is an Android mobile application built with Kotlin and Jetpack Compose that connects service providers (CONECTA_PRO) with clients (CLIENTE). The app enables clients to search, book, and review services, while providers can manage their service offerings, schedules, and reservations.

## Build Commands

### Build the project
```bash
./gradlew build
```

### Build debug APK
```bash
./gradlew assembleDebug
```

### Run tests
```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

### Clean build
```bash
./gradlew clean
```

## Architecture

### High-Level Structure

The app follows **Clean Architecture** with clear separation of concerns:

- **Data Layer** (`feature/*/data/`): Repositories and API services using Retrofit
- **Domain Layer** (`feature/*/domain/`): Use cases containing business logic
- **UI Layer** (`feature/*/ui/`): Compose screens and ViewModels

### Feature-Based Organization

The codebase is organized by features under `com.example.serviconnecta.feature`:

- **auth**: Login, registration, phone verification, identity verification
- **client**: Client-specific features (home, search, booking, reviews)
- **worker**: Service provider features (manage services, reservations, schedule)
- **shared**: Shared components (edit profile, change password, privacy/terms screens)

### Core Infrastructure

The `core` package contains shared infrastructure:

- **datastore**: DataStore-based preferences for auth tokens, user data, and location
  - `AuthPreferences`: Access/refresh tokens and account type
  - `UserPreferences`: User profile data
  - `LocationPreferences`: Saved location information

- **network**: Retrofit configuration and network monitoring
  - `RetrofitProvider`: Creates Retrofit instances with/without authentication
  - `AuthInterceptor`: Adds access token to authenticated requests
  - `NetworkMonitor`: Monitors network connectivity and shows `NoInternetDialog`
  - `NetworkConfig`: API base URL from BuildConfig

- **session**: Session management
  - `SessionManager`: Clears session data on logout

### Navigation Architecture

Three-level navigation hierarchy:

1. **MainNavGraph** (`feature/navigation/MainNavGraph.kt`): Root navigation
   - Routes: "auth", "worker", "client"
   - Manages authentication state and account type switching
   - Auto-login is currently **disabled** (see lines 55-76 for enabling it)

2. **AuthNavGraph** (`feature/auth/ui/navigation/AuthNavGraph.kt`): Authentication flow
   - Login → Registration → Phone verification → Identity verification
   - Uses `AuthDestination` sealed class for routes

3. **ClientNavGraph** / **WorkerNavGraph**: Feature-specific navigation
   - Bottom navigation tabs for each user type
   - Uses `AppDestination` sealed class for routes

### Dependency Injection Pattern

Dependencies are manually constructed in `MainActivity.onCreate()`:

1. Create DataStore instances for preferences
2. Build Retrofit instances (authenticated and non-authenticated)
3. Create API services from Retrofit
4. Create repositories from API services
5. Create use cases from repositories
6. Create ViewModels from use cases using factory pattern

**Note**: When adding new features, follow this pattern and create ViewModelProvider.Factory instances in MainActivity.

### Key Architectural Patterns

**Repository Pattern**: Each feature has a repository that encapsulates API calls and data transformations.

**Use Case Pattern**: Business logic is isolated in use cases (single responsibility functions).

**ViewModel Pattern**: UI state is managed through ViewModels with sealed class states.

**Response Wrapper**: API responses use `StandardResponse<T>` wrapper for consistent error handling.

### Authentication Flow

1. User logs in → `LoginUseCase` → `AuthRepository` saves tokens to `AuthPreferences`
2. Account type (CONECTA_PRO/CLIENTE) is saved and determines navigation path
3. `AuthInterceptor` reads access token from DataStore and adds to all authenticated requests
4. On logout → `SessionManager.clearSession()` → clears all DataStore preferences

### Identity Verification Flow

Multi-step process for user identity verification:

1. Overview → Select document type (from API options)
2. Camera capture → Submit document image (Base64 encoded)
3. Success confirmation

Located in `feature/auth/ui/identity/`. Uses CameraX for photo capture.

## API Integration

**Base URL**: Configured in `app/build.gradle.kts` as BuildConfig field
- Current: `https://backendconectapro-production.up.railway.app/`

**Request/Response Pattern**:
- DTOs in `feature/*/data/remote/*Dtos.kt`
- API services use Moshi for JSON serialization
- Logging interceptor enabled for debugging (logs full request/response bodies)

## State Management

**UI State Pattern**: ViewModels expose UI state through sealed classes or data classes with loading/success/error states.

Example pattern:
```kotlin
data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: SomeData? = null
)
```

**DataStore Flows**: Reactive state management for preferences using Kotlin Flow.

## Important Implementation Notes

### Network Connectivity
The app displays a blocking `NoInternetDialog` when network is unavailable. This is managed globally in `MainActivity` using `NetworkMonitor`.

### Image Handling
- CameraX is used for camera capture (identity verification)
- Images are converted to Base64 for API transmission
- Coil is used for image loading in Compose

### Date/Time Handling
Booking system uses date and time range selection. Format utilities are in `core/utils/FormatUtils.kt`.

### Location Management
Clients can manage multiple service locations stored via `LocationPreferences` and selected during booking.

## Testing Structure

- **Unit tests**: `app/src/test/` - Currently has example tests
- **Instrumented tests**: `app/src/androidTest/` - UI and integration tests

## Common Modifications

### Adding a new API endpoint
1. Define request/response DTOs in `feature/*/data/remote/*Dtos.kt`
2. Add method to appropriate `*ApiService` interface
3. Add repository method to handle the API call
4. Create a use case if needed
5. Update ViewModel to call the use case

### Adding a new screen
1. Create Composable in `feature/*/ui/`
2. Add route to appropriate `*Destination` sealed class
3. Add composable to navigation graph
4. Create ViewModel if needed with factory in MainActivity

### Modifying authentication
Auth logic is centralized in:
- `feature/auth/data/AuthRepository.kt`
- `core/datastore/AuthPreferences.kt`
- `core/network/AuthInterceptor.kt`

## Service Listing Implementation

### Getting All Services

The app displays services in two main ways:

1. **Home Screen** - Shows top 3 services sorted by rating (descending) then alphabetically
   - Uses `ClientHomeViewModel.loadHome()` → `getClientHomeUseCase()`
   - Endpoint: `GET /client/home` returns top services
   - ViewModel filters to show only top 3 using `.take(3)` (ClientHomeViewModel.kt:158)

2. **All Services Screen ("Ver más")** - Shows ALL available services
   - Uses `AllServicesViewModel.loadAllServices()` → `getAllServices()`
   - Endpoint: `GET /services/categories/{category_id}/services`
   - Process:
     1. Makes 3 API calls, one for each category:
        - Albañilería: `692b8dc198d59291c777649f`
        - Electricidad: `692b8dc198d59291c777649e`
        - Gasfitería: `692b8dc198d59291c777649d`
     2. Combines all services from all categories
     3. Removes duplicates by ID
     4. Returns the complete list
   - See `ClientServicesRepository.kt:581-659` for implementation

### Category-Based Filtering
- Endpoint: `GET /services/categories/{category_id}/services`
- Parameters: `page`, `page_size` (default 100), `search`
- Used by both `ServicesByCategoryViewModel` and `getAllServices()`
- Returns services specific to the given category ID