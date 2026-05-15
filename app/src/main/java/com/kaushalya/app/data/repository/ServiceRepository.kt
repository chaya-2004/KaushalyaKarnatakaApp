package com.kaushalya.app.data.repository

import com.kaushalya.app.data.dao.ServiceDao
import com.kaushalya.app.data.entities.ServiceEntity
import kotlinx.coroutines.flow.Flow

class ServiceRepository(private val serviceDao: ServiceDao) {

    fun getServicesForWorker(workerId: Int): Flow<List<ServiceEntity>> =
        serviceDao.getServicesForWorker(workerId)

    suspend fun insertService(service: ServiceEntity): Long = serviceDao.insertService(service)

    suspend fun updateService(service: ServiceEntity) = serviceDao.updateService(service)

    suspend fun deleteService(service: ServiceEntity) = serviceDao.deleteService(service)

    suspend fun getServiceById(serviceId: Int): ServiceEntity? =
        serviceDao.getServiceById(serviceId)
}
