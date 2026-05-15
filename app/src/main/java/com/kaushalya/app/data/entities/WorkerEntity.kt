package com.kaushalya.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a skilled worker's profile.
 */
@Entity(tableName = "workers")
data class WorkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,                  // FK to UserEntity
    val name: String,
    val phone: String,
    val category: String,             // electrician, plumber, etc.
    val address: String,
    val experience: Int,              // years of experience
    val aboutMe: String,
    val profileImageUri: String = "", // local URI string
    val averageRating: Float = 0f,
    val totalReviews: Int = 0,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
