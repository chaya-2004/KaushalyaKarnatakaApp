package com.kaushalya.app.ui.viewmodel

import androidx.lifecycle.*
import com.kaushalya.app.data.entities.ServiceEntity
import com.kaushalya.app.data.repository.ServiceRepository
import kotlinx.coroutines.launch

class ServiceViewModel(private val serviceRepository: ServiceRepository) : ViewModel() {

    private val _workerId = MutableLiveData<Int>()

    val services: LiveData<List<ServiceEntity>> = _workerId.switchMap { id ->
        serviceRepository.getServicesForWorker(id).asLiveData()
    }

    private val _operationResult = MutableLiveData<String>()
    val operationResult: LiveData<String> = _operationResult

    fun loadServicesForWorker(workerId: Int) {
        _workerId.value = workerId
    }

    fun insertService(service: ServiceEntity) {
        viewModelScope.launch {
            serviceRepository.insertService(service)
            _operationResult.value = "Service added successfully"
        }
    }

    fun updateService(service: ServiceEntity) {
        viewModelScope.launch {
            serviceRepository.updateService(service)
            _operationResult.value = "Service updated successfully"
        }
    }

    fun deleteService(service: ServiceEntity) {
        viewModelScope.launch {
            serviceRepository.deleteService(service)
            _operationResult.value = "Service deleted"
        }
    }
}
