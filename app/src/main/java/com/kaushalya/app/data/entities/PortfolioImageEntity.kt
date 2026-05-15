package com.kaushalya.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a portfolio image uploaded by a worker.
 */
@Entity(tableName = "portfolio_images")
data class PortfolioImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val workerId: Int,       // FK to WorkerEntity
    val imageUri: String,    // local URI string
    val caption: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
