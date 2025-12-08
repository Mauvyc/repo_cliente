package com.example.serviconnecta.feature.worker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.serviconnecta.feature.worker.data.WorkerRepository

class WorkerHomeViewModelFactory(
    private val repository: WorkerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkerHomeViewModel::class.java)) {
            return WorkerHomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}