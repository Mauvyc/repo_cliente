package com.example.serviconnecta.feature.client.ui.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.client.data.ClientServicesRepository
import com.example.serviconnecta.feature.client.domain.model.Location
import com.example.serviconnecta.feature.client.domain.model.PaymentMethod
import com.example.serviconnecta.feature.client.domain.model.PaymentType
import com.example.serviconnecta.feature.client.domain.model.ServiceItem
import com.example.serviconnecta.feature.client.domain.usecase.GetServiceDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookingUiState(
    val isLoading: Boolean = false,
    val service: ServiceItem? = null,

    // Ubicaciones
    val locations: List<Location> = emptyList(),
    val selectedLocationId: String? = null,

    // Pago
    val paymentMethods: List<PaymentMethod> = emptyList(),
    val selectedPaymentMethodId: String? = null,

    // Fecha, horario y comentario
    val selectedDate: String? = null,        // "2025-11-25"
    val selectedTimeStart: String? = null,   // "08:00"
    val selectedTimeEnd: String? = null,     // "09:00"
    val notes: String = "",

    // Resumen de precio
    val currency: String = "PEN",
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val total: Double = 0.0,

    // Estados de backend
    val isCreatingRequest: Boolean = false,
    val bookingCreated: Boolean = false,
    val error: String? = null,
    val showPaymentSuccess: Boolean = false,
)

class BookingViewModel(
    private val getServiceDetailUseCase: GetServiceDetailUseCase,
    private val clientServicesRepository: ClientServicesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState

    fun loadServiceDetail(serviceId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            runCatching {
                getServiceDetailUseCase(serviceId)
            }.onSuccess { service ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    service = service,
                    currency = "PEN", // si te llega del backend, úsalo
                    subtotal = service.price,
                    discount = 0.0,
                    total = service.price
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar servicio"
                )
            }
        }
    }

    // --- Ubicaciones ---
    fun loadLocations() {
        viewModelScope.launch {
            try {
                val locations = clientServicesRepository.getClientLocations()
                _uiState.value = _uiState.value.copy(
                    locations = locations,
                    // si quieres, NO toques aquí selectedLocationId,
                    // eso se maneja aparte
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun selectLocation(locationId: String) {
        _uiState.value = _uiState.value.copy(selectedLocationId = locationId)
    }

    // --- Métodos de pago ---
    fun loadPaymentMethods() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val methods = clientServicesRepository.getPaymentMethods()

                // 👇 DEBUG AQUÍ
                android.util.Log.d("BookingVM", "Métodos de pago desde repo: ${methods.size}")
                methods.forEach {
                    android.util.Log.d("BookingVM", "PaymentMethod -> id=${it.id}, type=${it.type}, label=${it.label}, last4=${it.last4}, isDefault=${it.isDefault}")
                }

                val defaultId = methods.firstOrNull { it.isDefault }?.id
                    ?: methods.firstOrNull()?.id

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    paymentMethods = methods,
                    selectedPaymentMethodId = _uiState.value.selectedPaymentMethodId ?: defaultId
                )

                // 👇 DEBUG DEL UI STATE
                android.util.Log.d(
                    "BookingVM",
                    "UiState.paymentMethods=${_uiState.value.paymentMethods.size}, selected=${_uiState.value.selectedPaymentMethodId}"
                )

            } catch (e: Exception) {
                android.util.Log.e("BookingVM", "Error cargando métodos de pago", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    paymentMethods = emptyList(),
                    error = e.message ?: "Error al obtener métodos de pago"
                )
            }
        }
    }

    fun selectPaymentMethod(paymentMethodId: String) {
        _uiState.value = _uiState.value.copy(selectedPaymentMethodId = paymentMethodId)
    }

    fun addCardPaymentMethod(
        cardNumber: String,
        cardHolderName: String,
        expMonth: Int,
        expYear: Int,
        cvv: String
    ) {
        viewModelScope.launch {
            try {
                // Crear en backend
                val newMethod = clientServicesRepository.createCardPaymentMethod(
                    cardNumber,
                    cardHolderName,
                    expMonth,
                    expYear,
                    cvv
                )

                // Volver a cargar lista desde backend para mantener todo consistente
                val methods = clientServicesRepository.getPaymentMethods()

                _uiState.update {
                    it.copy(
                        paymentMethods = methods,
                        selectedPaymentMethodId = newMethod.id,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Error al agregar tarjeta")
                }
            }
        }
    }

    // --- Fecha / hora / notas ---
    fun updateSchedule(date: String, start: String, end: String, notes: String) {
        _uiState.value = _uiState.value.copy(
            selectedDate = date,
            selectedTimeStart = start,
            selectedTimeEnd = end,
            notes = notes
        )
    }

    // --- Crear solicitud al backend ---
    fun createServiceRequest(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val current = _uiState.value
        val service = current.service
        val locationId = current.selectedLocationId
        val paymentMethodId = current.selectedPaymentMethodId
        val date = current.selectedDate
        val start = current.selectedTimeStart
        val end = current.selectedTimeEnd

        if (service == null || locationId == null || paymentMethodId == null || date == null || start == null || end == null) {
            onError("Faltan datos para crear la reserva")
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isCreatingRequest = true, error = null)

            runCatching {
                clientServicesRepository.createServiceRequest(
                    serviceId = service.id,
                    locationId = locationId,
                    scheduledDate = date,
                    timeStart = start,
                    timeEnd = end,
                    paymentMethodId = paymentMethodId,
                    notes = current.notes.ifBlank { null },
                    currency = current.currency,
                    subtotal = current.subtotal,
                    discount = current.discount,
                    total = current.total
                )
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isCreatingRequest = false,
                    bookingCreated = true,
                    showPaymentSuccess = true   // 👈 importante
                )
                onSuccess()
            }.onFailure { e ->
                val msg = e.message ?: "Error al crear la solicitud"
                _uiState.value = _uiState.value.copy(
                    isCreatingRequest = false,
                    error = msg
                )
                onError(msg)
            }
        }
    }

    fun confirmBooking() {
        val state = _uiState.value

        val serviceId = state.service?.id ?: return
        val locationId = state.selectedLocationId ?: return
        val paymentMethodId = state.selectedPaymentMethodId ?: return
        val date = state.selectedDate ?: return
        val start = state.selectedTimeStart ?: return
        val end = state.selectedTimeEnd ?: return

        viewModelScope.launch {
            try {
                _uiState.value = state.copy(
                    isLoading = true,
                    error = null
                )

                // Llamada al repositorio -> /client/service-request (POST)
                clientServicesRepository.createServiceRequest(
                    serviceId = serviceId,
                    locationId = locationId,
                    scheduledDate = date,
                    timeStart = start,
                    timeEnd = end,
                    paymentMethodId = paymentMethodId,
                    notes = state.notes,
                    currency = state.currency,
                    subtotal = state.subtotal,
                    discount = state.discount,
                    total = state.total
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    bookingCreated = true,
                    showPaymentSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al crear la solicitud"
                )
            }
        }
    }

    fun addNewCard(
        cardNumber: String,
        cardHolderName: String,
        expMonth: Int,
        expYear: Int,
        cvv: String
    ) {
        viewModelScope.launch {
            // opcional: mostrar loading mientras se crea
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                // 1) Llamar al backend para crear la tarjeta
                val newMethod = clientServicesRepository.createCardPaymentMethod(
                    cardNumber = cardNumber,
                    cardHolderName = cardHolderName,
                    expMonth = expMonth,
                    expYear = expYear,
                    cvv = cvv
                )

                // 2) Actualizar la lista y seleccionar la nueva tarjeta
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    paymentMethods = _uiState.value.paymentMethods + newMethod,
                    selectedPaymentMethodId = newMethod.id,
                    error = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al agregar tarjeta"
                )
            }
        }
    }
    fun onPaymentSuccessHandled() {
        _uiState.update { it.copy(showPaymentSuccess = false) }
    }
}