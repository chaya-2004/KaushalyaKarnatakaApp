package com.kaushalya.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a service offered by a worker.
 */
@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val workerId: Int,          // FK to WorkerEntity
    val serviceName: String,
    val description: String,
    val price: Double,
    val priceType: String,      // "Fixed" or "Starting from"
    val createdAt: Long = System.currentTimeMillis()
)
