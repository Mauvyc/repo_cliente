package com.example.serviconnecta.feature.worker.data.remote

import com.example.serviconnecta.core.network.StandardResponse
import com.example.serviconnecta.feature.worker.domain.model.WorkerHomeResponse
import retrofit2.http.GET

interface WorkerApiService {

    @GET("/provider/home")
    suspend fun getHomeData(): StandardResponse<WorkerHomeResponse>
}