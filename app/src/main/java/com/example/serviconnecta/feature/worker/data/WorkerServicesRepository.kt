package com.example.serviconnecta.feature.worker.data

import com.example.serviconnecta.feature.worker.data.remote.HomeMappers
import com.example.serviconnecta.feature.worker.data.remote.WorkerApiService
import com.example.serviconnecta.feature.worker.data.remote.WorkerHomeResponseDto
import com.example.serviconnecta.feature.worker.domain.model.WorkerHome
import com.example.serviconnecta.feature.worker.domain.model.WorkerHomeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface WorkerRepository {
    suspend fun getWorkerHomeData(): WorkerHomeResponse
}

class WorkerRepositoryImpl(
    private val apiService: WorkerApiService,
    private val homeMappers: HomeMappers = HomeMappers()
) : WorkerRepository {

    override suspend fun getWorkerHomeData(): WorkerHomeResponse {
        return withContext(Dispatchers.IO) {
            val response = apiService.getHomeData()

            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception("Error al obtener los datos del trabajador: ${response.message}")
            }
        }
    }
}

