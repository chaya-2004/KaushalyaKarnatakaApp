package com.kaushalya.app.data.dao

import androidx.room.*
import com.kaushalya.app.data.entities.ServiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceEntity): Long

    @Update
    suspend fun updateService(service: ServiceEntity)

    @Delete
    suspend fun deleteService(service: ServiceEntity)

    @Query("SELECT * FROM services WHERE workerId = :workerId ORDER BY createdAt DESC")
    fun getServicesForWorker(workerId: Int): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE id = :serviceId LIMIT 1")
    suspend fun getServiceById(serviceId: Int): ServiceEntity?

    @Query("DELETE FROM services WHERE workerId = :workerId")
    suspend fun deleteAllServicesForWorker(workerId: Int)
}
