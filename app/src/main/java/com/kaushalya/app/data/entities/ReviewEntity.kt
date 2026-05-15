package com.kaushalya.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a customer review for a worker.
 */
@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val workerId: Int,       // FK to WorkerEntity
    val reviewerName: String,
    val rating: Float,       // 1.0 to 5.0
    val comment: String,
    val createdAt: Long = System.currentTimeMillis()
)
